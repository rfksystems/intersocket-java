package com.rfksystems.intersocket.frames;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rfksystems.intersocket.MessageType;

public class S2CAcknowledgeFrame {
    @JsonProperty("ack")
    private final String acknowledgeId;

    private S2CAcknowledgeFrame(final String acknowledgeId) {
        this.acknowledgeId = acknowledgeId;
    }

    public static S2CAcknowledgeFrameBuilder builder() {
        return new S2CAcknowledgeFrameBuilder();
    }

    @JsonIgnore
    public String getAcknowledgeId() {
        return acknowledgeId;
    }

    @Override
    public String toString() {
        return String.format("%s{ack='%s'}", MessageType.S2C_ACK, acknowledgeId);
    }

    public static final class S2CAcknowledgeFrameBuilder {
        private String acknowledgeId;

        private S2CAcknowledgeFrameBuilder() {
        }

        public S2CAcknowledgeFrameBuilder withAcknowledgeId(final String acknowledgeId) {
            this.acknowledgeId = acknowledgeId;
            return this;
        }

        public S2CAcknowledgeFrame build() {
            return new S2CAcknowledgeFrame(acknowledgeId);
        }
    }
}
