package com.rfksystems.intersocket.frames;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.rfksystems.intersocket.MessageType;

public class S2CBroadcastFrame {
    @JsonProperty("topic")
    private final String topic;

    @JsonProperty("broadcast")
    private final JsonNode broadcast;

    private S2CBroadcastFrame(final String topic, final JsonNode broadcast) {
        this.topic = topic;
        this.broadcast = broadcast;
    }

    public static S2CBroadcastFrameBuilder builder() {
        return new S2CBroadcastFrameBuilder();
    }

    @JsonIgnore
    public String getTopic() {
        return topic;
    }

    @JsonIgnore
    public JsonNode getBroadcast() {
        return broadcast;
    }

    @Override
    public String toString() {
        return String.format("%s{topic='%s', broadcast=[TRUNCATED]}", MessageType.S2C_BROADCAST, topic);
    }

    public static final class S2CBroadcastFrameBuilder {
        private String topic;
        private JsonNode broadcast;

        private S2CBroadcastFrameBuilder() {
        }

        public S2CBroadcastFrameBuilder withTopic(final String topic) {
            this.topic = topic;
            return this;
        }

        public S2CBroadcastFrameBuilder withBroadcast(final JsonNode broadcast) {
            this.broadcast = broadcast;
            return this;
        }

        public S2CBroadcastFrame build() {
            return new S2CBroadcastFrame(topic, broadcast);
        }
    }
}
