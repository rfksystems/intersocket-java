package com.rfksystems.intersocket.bundled;

import com.fasterxml.jackson.databind.JsonNode;
import com.rfksystems.intersocket.MessageHandler;

public class DefaultLostMessageHandler<T> implements MessageHandler<T> {
    @Override
    public Object handle(final JsonNode payload, final T scope) {
        return null;
    }
}
