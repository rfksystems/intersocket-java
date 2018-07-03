package com.rfksystems.intersocket.bundled;

import com.rfksystems.intersocket.HandshakeHandler;
import com.rfksystems.intersocket.response.HandshakeResponse;

import java.util.function.Consumer;

public class DefaultHandshakeHandler<T> implements HandshakeHandler<T> {
    private static final HandshakeResponse DEFAULT = HandshakeResponse.builder().build();

    @Override
    public void handle(final T scope, final Consumer<HandshakeResponse> responseHandler) {
        responseHandler.accept(DEFAULT);
    }
}
