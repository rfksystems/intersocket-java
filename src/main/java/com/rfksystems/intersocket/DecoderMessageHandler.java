package com.rfksystems.intersocket;

import com.fasterxml.jackson.databind.JsonNode;

public interface DecoderMessageHandler<T, R> extends MessageHandler<T> {
    @Override
    default Object handle(final JsonNode payload, final T scope) {
        return handle(decodePayload(payload), scope);
    }

    Object handle(final R payload, final T scope);

    R decodePayload(final JsonNode payload);
}
