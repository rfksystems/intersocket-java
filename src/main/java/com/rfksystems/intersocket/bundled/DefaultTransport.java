package com.rfksystems.intersocket.bundled;

import com.rfksystems.intersocket.Transport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultTransport<T> implements Transport<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTransport.class);

    @Override
    public void send(final String message, final T scope) {
        LOGGER.warn("Using default transport to send {} in scope {}", message, scope);
    }
}
