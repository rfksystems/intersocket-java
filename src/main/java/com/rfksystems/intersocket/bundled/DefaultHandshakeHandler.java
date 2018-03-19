package com.rfksystems.intersocket.bundled;

import com.rfksystems.intersocket.HandshakeHandler;
import com.rfksystems.intersocket.response.HandshakeResponse;

public class DefaultHandshakeHandler<T> implements HandshakeHandler<T> {
    private static final HandshakeResponse DEFAULT = HandshakeResponse.builder().build();

    @Override
    public HandshakeResponse handle(final T scope) {
        return DEFAULT;
    }
}
