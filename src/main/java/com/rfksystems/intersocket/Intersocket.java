package com.rfksystems.intersocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rfksystems.intersocket.bundled.DefaultHandshakeHandler;
import com.rfksystems.intersocket.bundled.DefaultLostMessageHandler;
import com.rfksystems.intersocket.bundled.DefaultTopicsSynchronizeHandler;
import com.rfksystems.intersocket.bundled.DefaultTransport;
import com.rfksystems.intersocket.frames.*;
import com.rfksystems.intersocket.response.ErrorResponse;
import com.rfksystems.intersocket.response.HandshakeResponse;
import com.rfksystems.intersocket.response.ResponseWithAttachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import static com.rfksystems.intersocket.MessageType.*;

public class Intersocket<T> {
    public static final short MAGIC_ID = 463;
    public static final String MAGIC_ID_STRING = String.valueOf(MAGIC_ID);
    private static final Logger LOGGER = LoggerFactory.getLogger(Intersocket.class);

    private final Map<String, MessageHandler<T>> handlers;
    private final MessageHandler<T> lostMessageHandler;
    private final HandshakeHandler<T> handshakeHandler;
    private final TopicsSynchronizeHandler<T> topicsSynchronizeHandler;
    private final Transport<T> transport;
    private final ObjectMapper objectMapper;
    private final SubscriberRegistry<T> subscriberRegistry = new SubscriberRegistry<>();
    private final Set<T> scopes = new HashSet<>();

    private Intersocket(
            final Map<String, MessageHandler<T>> messageHandlers,
            final MessageHandler<T> lostMessageHandler,
            final HandshakeHandler<T> handshakeHandler,
            final TopicsSynchronizeHandler<T> topicsSynchronizeHandler,
            final Transport<T> transport,
            final ObjectMapper objectMapper
    ) {
        this.handlers = messageHandlers;
        this.lostMessageHandler = lostMessageHandler;
        this.handshakeHandler = handshakeHandler;
        this.topicsSynchronizeHandler = topicsSynchronizeHandler;
        this.transport = transport;
        this.objectMapper = objectMapper;
    }

    public static <S> IntersocketBuilder<S> builder(final Class<S> scopeClass) {
        return new IntersocketBuilder<>();
    }

    public boolean isIntersocketFrame(final String message) {
        if (!message.startsWith(MAGIC_ID_STRING)) {
            return false;
        }

        final String typeCode = getTypeCodeFromMessage(message);

        return typeCode.equals(C2S_HANDSHAKE.toStringCode())
                || typeCode.equals(C2S_MESSAGE.toStringCode())
                || typeCode.equals(C2S_SYNCHRONIZE_TOPICS.toStringCode());
    }

    public Intersocket<T> handleMessage(final String message, final T scope) throws UnknownMessageFormatException {
        if (!isIntersocketFrame(message)) {
            throw new UnknownMessageFormatException();
        }

        final String typeCode = getTypeCodeFromMessage(message);
        final MessageType messageType = MessageType.STRING_TYPE_MAP.get(typeCode);

        if (messageType == MessageType.C2S_HANDSHAKE) {
            handleHandshake(message, scope);
            return this;
        }

        if (messageType == MessageType.C2S_MESSAGE) {
            handleClientMessage(message, scope);
            return this;
        }

        if (messageType == MessageType.C2S_SYNCHRONIZE_TOPICS) {
            handleClientTopicsSync(message, scope);
            return this;
        }

        throw new RuntimeException();
    }

    public Intersocket<T> sendIdentUpdate(final Object newIdent, final T scope) {
        final S2CIdentChangeFrame identChangeMessage = S2CIdentChangeFrame.builder()
                .withNewIdent(objectMapper.valueToTree(newIdent))
                .build();

        sendRaw(S2C_IDENT_CHANGE, identChangeMessage, scope);

        return this;
    }

    public Intersocket<T> sendPlatformUpdate(final String newPlatformVersion) {
        final S2CPlatformChangeFrame identChangeMessage = S2CPlatformChangeFrame.builder()
                .withNewPlatformVersion(newPlatformVersion)
                .build();

        synchronized (scopes) {
            for (final T scope : scopes) {
                sendRaw(S2C_PLATFORM_CHANGE, identChangeMessage, scope);
            }
        }

        return this;
    }

    public Intersocket<T> broadcast(final String topic, final Object payload) {
        return broadcast(topic, payload, scope -> true);
    }

    public Intersocket<T> broadcast(final String topic, final Object payload, final Predicate<T> filter) {
        final Set<T> scopes = subscriberRegistry.forTopic(topic);

        if (scopes.isEmpty()) {
            return this;
        }

        final S2CBroadcastFrame frame = S2CBroadcastFrame.builder()
                .withTopic(topic)
                .withBroadcast(objectMapper.valueToTree(payload))
                .build();

        Throwable firstError = null;
        for (final T scope : scopes) {
            if (!filter.test(scope)) {
                continue;
            }

            try {
                sendRaw(S2C_BROADCAST, frame, scope);
            } catch (final Throwable error) {
                if (null == firstError) {
                    firstError = error;
                }
            }
        }

        if (null != firstError) {
            throw new RuntimeException(firstError);
        }

        return this;
    }

    public Intersocket<T> broadcastToScope(final String topic, final Object payload, final T scope) {
        final S2CBroadcastFrame frame = S2CBroadcastFrame.builder()
                .withTopic(topic)
                .withBroadcast(objectMapper.valueToTree(payload))
                .build();

        sendRaw(S2C_BROADCAST, frame, scope);

        return this;
    }

    public void sendRaw(final MessageType type, final Object objectToEncode, final T scope) {
        try {
            LOGGER.debug("Sending {} to scope {}", objectToEncode, scope);
            final String text = objectMapper.writeValueAsString(objectToEncode);
            transport.send(MAGIC_ID_STRING + type.toStringCode() + text, scope);

        } catch (final JsonProcessingException e) {
            LOGGER.error("Failed to encode message", e);
        } catch (final Throwable t) {
            LOGGER.error("Failed to send message", t);
        }
    }

    public void sendBinary(final byte[] payload, final T scope) {
        try {
            transport.sendBinary(payload, scope);
        } catch (final Throwable t) {
            LOGGER.error("Failed to send binary message", t);
        }
    }

    public Intersocket<T> removeScope(final T scope) {
        subscriberRegistry.clear(scope);

        synchronized (scopes) {
            scopes.remove(scope);
        }

        return this;
    }

    private void handleClientTopicsSync(final String message, final T scope) {
        final C2SSynchronizeTopicsFrame topicsFrame = decodeMessagePayload(
                message,
                C2SSynchronizeTopicsFrame.class
        );
        subscriberRegistry.sync(topicsFrame.getTopics(), scope);
        topicsSynchronizeHandler.handle(topicsFrame.getTopics(), scope);

    }

    private void handleClientMessage(final String message, final T scope) {
        final C2SMessageFrame frame = decodeMessagePayload(message, C2SMessageFrame.class);
        final String topic = frame.getTopic();

        sendAcknowledged(frame, scope);

        final Object response;

        try {
            if (null != topic && handlers.containsKey(topic)) {
                final MessageHandler<T> messageHandler = handlers.get(topic);
                response = messageHandler.handle(frame.getPayload(), scope);
                LOGGER.debug("Used topic defined handler for message response for {}:{}", topic, frame.getId());
            } else {
                response = lostMessageHandler.handle(frame.getPayload(), scope);
                LOGGER.debug(
                        "Used lost message handle for message response {}:{}",
                        topic,
                        frame.getId()
                );
            }
        } catch (final Throwable throwable) {
            sendRaw(S2C_MESSAGE_ERROR, S2CMessageErrorFrame.unhandled(frame.getId(), throwable), scope);
            LOGGER.error(
                    String.format(
                            "Unhandled exception while handling message %s:%s, sending error response",
                            topic,
                            frame.getId()
                    ),
                    throwable
            );
            return;
        }

        if (frame.isNotification()) {
            // This is a notification, so we don't care
            return;
        }

        if (response instanceof ErrorResponse) {
            final ErrorResponse errorResponse = (ErrorResponse) response;

            final S2CMessageErrorFrame messageErrorFrame = S2CMessageErrorFrame.builder()
                    .withId(frame.getId())
                    .withCode(errorResponse.getCode())
                    .withDescription(errorResponse.getDescription())
                    .build();

            sendRaw(S2C_MESSAGE_ERROR, messageErrorFrame, scope);
            return;
        }

        if (response instanceof ResponseWithAttachment) {
            final ResponseWithAttachment responseWithAttachment = (ResponseWithAttachment) response;

            sendAttachment(frame, responseWithAttachment.getAttachment(), scope);
            sendMessageResponse(frame, responseWithAttachment.getResponse(), scope);

            return;
        }

        sendMessageResponse(frame, response, scope);
    }

    private void sendAcknowledged(final C2SMessageFrame frame, final T scope) {
        final S2CAcknowledgeFrame acknowledgeFrame = S2CAcknowledgeFrame.builder()
                .withAcknowledgeId(frame.getId())
                .build();
        sendRaw(MessageType.S2C_ACK, acknowledgeFrame, scope);
    }

    private void handleHandshake(final String message, final T scope) {
        final C2SHandshakeFrame frame = decodeMessagePayload(message, C2SHandshakeFrame.class);

        if (frame.getProtocolVersion() != (short) 1) {
            final S2CErrorFrame errorFrame = S2CErrorFrame.builder()
                    .withCode(S2CErrorFrame.ERROR_CODE.PROTOCOL_MISMATCH)
                    .withDescription("Protocol unsupported")
                    .build();
            sendRaw(S2C_ERROR, errorFrame, scope);
            return;
        }

        final HandshakeResponse response = handshakeHandler.handle(scope);

        final S2CHandshakeFrame handshakeFrame = S2CHandshakeFrame.builder()
                .withProtocolVersion((short) 1)
                .withIdent(objectMapper.valueToTree(response.getIdent()))
                .withPlatformVersion(response.getPlatformVersion())
                .build();

        synchronized (scopes) {
            scopes.add(scope);
        }

        sendRaw(MessageType.S2C_HANDSHAKE, handshakeFrame, scope);
    }

    private void sendMessageResponse(final C2SMessageFrame frame, final Object response, final T scope) {
        final JsonNode payload = objectMapper.valueToTree(response);

        final S2CMessageFrame responseFrame = S2CMessageFrame.builder()
                .withId(frame.getId())
                .withPayload(payload)
                .build();

        sendRaw(MessageType.S2C_MESSAGE, responseFrame, scope);
    }

    private void sendAttachment(final C2SMessageFrame frame, final byte[] attachment, final T scope) {
        final String headerText = MessageType.S2C_BINARY_ATTACHMENT.toPrefix()
                + frame.getId();

        final byte[] header = headerText.getBytes(Charset.forName("ASCII"));
        final byte[] payload = new byte[header.length + attachment.length];

        System.arraycopy(header, 0, payload, 0, header.length);
        System.arraycopy(attachment, 0, payload, header.length, attachment.length);

        sendBinary(payload, scope);
    }

    private <P> P decodeMessagePayload(final String message, final Class<P> tClass) {
        try {
            return objectMapper.readValue(message.substring(5), tClass);
        } catch (final IOException e) {
            LOGGER.error("Failed to decode payload", e);
            throw new RuntimeException(e);
        }
    }

    private String getTypeCodeFromMessage(final String message) {
        return message.substring(3, 5);
    }


    public static final class IntersocketBuilder<S> {
        private Map<String, MessageHandler<S>> handlers = new ConcurrentHashMap<>();
        private MessageHandler<S> lostMessageHandler = new DefaultLostMessageHandler<>();
        private Transport<S> transport = new DefaultTransport<>();
        private HandshakeHandler<S> handshakeHandler = new DefaultHandshakeHandler<>();
        private TopicsSynchronizeHandler<S> topicsSynchronizeHandler = new DefaultTopicsSynchronizeHandler<>();
        private ObjectMapper objectMapper = new ObjectMapper();

        private IntersocketBuilder() {
        }

        public IntersocketBuilder<S> onLostMessage(final MessageHandler<S> lostMessageHandler) {
            if (null == lostMessageHandler) {
                throw new NullPointerException();
            }
            this.lostMessageHandler = lostMessageHandler;
            return this;
        }

        public IntersocketBuilder<S> withTransport(final Transport<S> transport) {
            if (null == transport) {
                throw new NullPointerException();
            }
            this.transport = transport;
            return this;
        }

        public IntersocketBuilder<S> onHandshake(final HandshakeHandler<S> handshakeHandler) {
            if (null == handshakeHandler) {
                throw new NullPointerException();
            }
            this.handshakeHandler = handshakeHandler;
            return this;
        }

        public IntersocketBuilder<S> onTopicsSync(final TopicsSynchronizeHandler<S> topicsSynchronizeHandler) {
            if (null == topicsSynchronizeHandler) {
                throw new NullPointerException();
            }
            this.topicsSynchronizeHandler = topicsSynchronizeHandler;
            return this;
        }

        public IntersocketBuilder<S> withObjectMapper(final ObjectMapper objectMapper) {
            if (null == objectMapper) {
                throw new NullPointerException();
            }
            this.objectMapper = objectMapper;
            return this;
        }

        public IntersocketBuilder<S> withHandlers(final Map<String, MessageHandler<S>> handlers) {
            this.handlers = handlers;
            return this;
        }

        public IntersocketBuilder<S> onMessage(final String topic, final MessageHandler<S> handler) {
            if (null == topic || null == handler) {
                throw new NullPointerException();
            }

            if (handlers.containsKey(topic)) {
                throw new RuntimeException(String.format("Attempt to assign multiple handlers for topic %s", topic));
            }

            handlers.put(topic, handler);

            return this;
        }

        public Intersocket<S> build() {
            return new Intersocket<>(
                    handlers,
                    lostMessageHandler,
                    handshakeHandler,
                    topicsSynchronizeHandler,
                    transport,
                    objectMapper
            );
        }
    }
}

