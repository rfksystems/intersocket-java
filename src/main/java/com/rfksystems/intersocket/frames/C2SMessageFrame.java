package com.rfksystems.intersocket.frames;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.rfksystems.intersocket.MessageType;

public class C2SMessageFrame {
    private final String id;
    private final String topic;
    private final JsonNode payload;
    private final boolean isNotification;

    @JsonCreator
    public C2SMessageFrame(
            @JsonProperty("id") final String id,
            @JsonProperty("topic") final String topic,
            @JsonProperty("payload") final JsonNode payload,
            @JsonProperty("notify") final boolean isNotification
    ) {
        this.id = id;
        this.topic = topic;
        this.payload = payload;
        this.isNotification = isNotification;
    }

    @JsonIgnore
    public String getId() {
        return id;
    }

    @JsonIgnore
    public String getTopic() {
        return topic;
    }

    @JsonIgnore
    public JsonNode getPayload() {
        return payload;
    }

    @JsonIgnore
    public boolean isNotification() {
        return isNotification;
    }

    @Override
    public String toString() {
        return String.format(
                "%s{id='%s', topic='%s', payload=[TRUNCATED], notify=%s}",
                MessageType.C2S_MESSAGE,
                id,
                topic,
                isNotification
        );
    }
}
