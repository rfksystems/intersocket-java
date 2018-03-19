package com.rfksystems.intersocket.frames;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rfksystems.intersocket.MessageType;

public class S2CPlatformChangeFrame {
    @JsonProperty("platformChange")
    private final String newPlatformVersion;

    private S2CPlatformChangeFrame(final String newPlatformVersion) {
        this.newPlatformVersion = newPlatformVersion;
    }

    public static S2CPlatformChangeFrameBuilder builder() {
        return new S2CPlatformChangeFrameBuilder();
    }

    @JsonIgnore
    public String getNewPlatformVersion() {
        return newPlatformVersion;
    }

    @Override
    public String toString() {
        return String.format("%s{platformChange='%s'}", MessageType.S2C_PLATFORM_CHANGE, newPlatformVersion);
    }

    public static final class S2CPlatformChangeFrameBuilder {
        private String newPlatformVersion;

        private S2CPlatformChangeFrameBuilder() {
        }

        public S2CPlatformChangeFrameBuilder withNewPlatformVersion(final String newPlatformVersion) {
            this.newPlatformVersion = newPlatformVersion;
            return this;
        }

        public S2CPlatformChangeFrame build() {
            return new S2CPlatformChangeFrame(newPlatformVersion);
        }
    }
}
