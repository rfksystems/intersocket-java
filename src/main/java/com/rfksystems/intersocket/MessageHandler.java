package com.rfksystems.intersocket;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.function.Consumer;

@FunctionalInterface
public interface MessageHandler<T> {
    void handle(final JsonNode payload, final T scope, final Consumer<Object> onResult);
}
