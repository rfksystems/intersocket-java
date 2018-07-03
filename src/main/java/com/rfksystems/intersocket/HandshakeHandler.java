package com.rfksystems.intersocket;

import com.rfksystems.intersocket.response.HandshakeResponse;

import java.util.function.Consumer;

@FunctionalInterface
public interface HandshakeHandler<T> {
    void handle(final T scope, final Consumer<HandshakeResponse> responseHandler);
}
