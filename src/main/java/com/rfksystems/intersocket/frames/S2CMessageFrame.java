package com.rfksystems.intersocket.frames;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.rfksystems.intersocket.MessageType;

public class S2CMessageFrame {
    @JsonProperty("id")
    private final String id;

    @JsonProperty("payload")
    private final JsonNode payload;

    S2CMessageFrame(final String id, final JsonNode payload) {
        this.id = id;
        this.payload = payload;
    }

    public static S2CMessageFrameBuilder builder() {
        return new S2CMessageFrameBuilder();
    }

    @JsonIgnore
    public String getId() {
        return id;
    }

    @JsonIgnore
    public JsonNode getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return String.format("%s{id='%s', payload=[TRUNCATED]}", MessageType.S2C_MESSAGE, id);
    }

    public static final class S2CMessageFrameBuilder {
        private String id;
        private JsonNode payload;

        private S2CMessageFrameBuilder() {
        }

        public S2CMessageFrameBuilder withId(final String id) {
            this.id = id;
            return this;
        }

        public S2CMessageFrameBuilder withPayload(final JsonNode payload) {
            this.payload = payload;
            return this;
        }

        public S2CMessageFrame build() {
            return new S2CMessageFrame(id, payload);
        }
    }
}
