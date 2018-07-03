package com.rfksystems.intersocket.bundled;

import com.fasterxml.jackson.databind.JsonNode;
import com.rfksystems.intersocket.MessageHandler;

import java.util.function.Consumer;

public class DefaultLostMessageHandler<T> implements MessageHandler<T> {
    @Override
    public void handle(final JsonNode payload, final T scope, final Consumer<Object> resultHandler) {
        resultHandler.accept(null);
    }
}
