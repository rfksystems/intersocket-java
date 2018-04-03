package com.rfksystems.intersocket.frames;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.rfksystems.intersocket.MessageType;

public class S2CHandshakeFrame {
    @JsonProperty("protocol")
    private final short protocolVersion;

    @JsonProperty("platform")
    private final String platformVersion;

    @JsonProperty("ident")
    private final JsonNode ident;

    private S2CHandshakeFrame(
            final short protocolVersion,
            final String platformVersion,
            final JsonNode ident
    ) {
        this.protocolVersion = protocolVersion;
        this.platformVersion = platformVersion;
        this.ident = ident;
    }

    public static S2CHandshakeFrameBuilder builder() {
        return new S2CHandshakeFrameBuilder();
    }

    @JsonIgnore
    public short getProtocolVersion() {
        return protocolVersion;
    }

    @JsonIgnore
    public String getPlatformVersion() {
        return platformVersion;
    }

    @JsonIgnore
    public JsonNode getIdent() {
        return ident;
    }

    @Override
    public String toString() {
        return String.format(
                "%s{protocol=%s, platform='%s', ident='%s'}",
                MessageType.S2C_HANDSHAKE,
                protocolVersion,
                platformVersion,
                ident
        );
    }

    public static final class S2CHandshakeFrameBuilder {
        private short protocolVersion;
        private String platformVersion;
        private JsonNode ident;

        private S2CHandshakeFrameBuilder() {
        }

        public S2CHandshakeFrameBuilder withProtocolVersion(final short protocolVersion) {
            this.protocolVersion = protocolVersion;
            return this;
        }

        public S2CHandshakeFrameBuilder withPlatformVersion(final String platformVersion) {
            this.platformVersion = platformVersion;
            return this;
        }

        public S2CHandshakeFrameBuilder withIdent(final JsonNode ident) {
            this.ident = ident;
            return this;
        }

        public S2CHandshakeFrame build() {
            return new S2CHandshakeFrame(protocolVersion, platformVersion, ident);
        }
    }
}
