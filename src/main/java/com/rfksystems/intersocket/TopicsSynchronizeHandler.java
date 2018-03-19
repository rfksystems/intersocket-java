package com.rfksystems.intersocket;

import java.util.Set;

@FunctionalInterface
public interface TopicsSynchronizeHandler<T> {
    void handle(final Set<String> topics, final T scope);
}
