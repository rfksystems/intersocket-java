package com.rfksystems.intersocket;

import com.rfksystems.intersocket.response.HandshakeResponse;

@FunctionalInterface
public interface HandshakeHandler<T> {
    HandshakeResponse handle(final T scope);
}
