package com.rfksystems.intersocket;

public interface Transport<T> {
    void send(final String message, final T scope);

    default void sendBinary(byte[] payload, final T scope) {
        throw new UnsupportedOperationException("sendBinary is not implemented for this transport");
    }
}
