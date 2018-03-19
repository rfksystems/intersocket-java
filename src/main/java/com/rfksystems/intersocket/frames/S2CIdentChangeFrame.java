package com.rfksystems.intersocket.frames;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rfksystems.intersocket.MessageType;

public class S2CIdentChangeFrame {
    @JsonProperty("identChange")
    private final String newIdent;

    private S2CIdentChangeFrame(final String newIdent) {
        this.newIdent = newIdent;
    }

    public static S2CIdentChangeFrameBuilder builder() {
        return new S2CIdentChangeFrameBuilder();
    }

    @JsonIgnore
    public String getNewIdent() {
        return newIdent;
    }

    @Override
    public String toString() {
        return String.format("%s{identChange='%s'}", MessageType.S2C_IDENT_CHANGE, newIdent);
    }

    public static final class S2CIdentChangeFrameBuilder {
        private String newIdent;

        private S2CIdentChangeFrameBuilder() {
        }

        public S2CIdentChangeFrameBuilder withNewIdent(final String newIdent) {
            this.newIdent = newIdent;
            return this;
        }

        public S2CIdentChangeFrame build() {
            return new S2CIdentChangeFrame(newIdent);
        }
    }
}
