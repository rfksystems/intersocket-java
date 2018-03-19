package com.rfksystems.intersocket;

import com.fasterxml.jackson.databind.JsonNode;

@FunctionalInterface
public interface MessageHandler<T> {
    Object handle(final JsonNode payload, final T scope);
}
