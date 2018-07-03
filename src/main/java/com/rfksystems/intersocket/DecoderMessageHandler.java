package com.rfksystems.intersocket;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.function.Consumer;

public interface DecoderMessageHandler<T, R> extends MessageHandler<T> {
    @Override
    default void handle(final JsonNode payload, final T scope, final Consumer<Object> resultHandler) {
        handle(decodePayload(payload), scope, resultHandler);
    }

    void handle(final R payload, final T scope, final Consumer<Object> resultHandler);

    R decodePayload(final JsonNode payload);
}
