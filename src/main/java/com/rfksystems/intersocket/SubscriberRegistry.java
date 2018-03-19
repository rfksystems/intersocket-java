package com.rfksystems.intersocket;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class SubscriberRegistry<T> {
    private final ConcurrentHashMap<T, Set<String>> subscribers = new ConcurrentHashMap<>();

    public void sync(final Set<String> topics, final T scope) {
        synchronized (subscribers) {
            subscribers.put(scope, topics);
        }
    }

    public void clear(final T scope) {
        subscribers.remove(scope);
    }

    public Set<T> forTopic(final String topic) {
        final Set<T> targets = new HashSet<>();

        subscribers.forEach((scope, topics) -> {
            if (topics.contains(topic)) {
                targets.add(scope);
            }
        });

        return targets;
    }
}
