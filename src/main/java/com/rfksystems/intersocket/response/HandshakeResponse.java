package com.rfksystems.intersocket.response;

public class HandshakeResponse {
    private final String platformVersion;
    private final String ident;

    private HandshakeResponse(final String platformVersion, final String ident) {
        this.platformVersion = platformVersion;
        this.ident = ident;
    }

    public static HandshakeResponseBuilder builder() {
        return new HandshakeResponseBuilder();
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public String getIdent() {
        return ident;
    }

    public static final class HandshakeResponseBuilder {
        private String platformVersion;
        private String ident;

        private HandshakeResponseBuilder() {
        }

        public HandshakeResponseBuilder withPlatformVersion(String platformVersion) {
            this.platformVersion = platformVersion;
            return this;
        }

        public HandshakeResponseBuilder withIdent(String ident) {
            this.ident = ident;
            return this;
        }

        public HandshakeResponse build() {
            return new HandshakeResponse(platformVersion, ident);
        }
    }
}
