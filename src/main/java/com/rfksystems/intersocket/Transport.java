package com.rfksystems.intersocket;

public interface Transport<T> {
    void send(final String message, final T scope);
}
