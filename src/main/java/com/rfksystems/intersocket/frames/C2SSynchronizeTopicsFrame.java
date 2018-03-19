package com.rfksystems.intersocket.frames;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rfksystems.intersocket.MessageType;

import java.util.Set;

public class C2SSynchronizeTopicsFrame {
    private final Set<String> topics;

    @JsonCreator
    public C2SSynchronizeTopicsFrame(@JsonProperty("topics") final Set<String> topics) {
        this.topics = topics;
    }

    public Set<String> getTopics() {
        return topics;
    }

    @Override
    public String toString() {
        return String.format("%s{topics=%s}", MessageType.C2S_SYNCHRONIZE_TOPICS, topics);
    }
}
