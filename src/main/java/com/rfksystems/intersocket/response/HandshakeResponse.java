package com.rfksystems.intersocket.response;

public class HandshakeResponse {
    private final String platformVersion;
    private final Object ident;

    private HandshakeResponse(final String platformVersion, final Object ident) {
        this.platformVersion = platformVersion;
        this.ident = ident;
    }

    public static HandshakeResponseBuilder builder() {
        return new HandshakeResponseBuilder();
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public Object getIdent() {
        return ident;
    }

    public static final class HandshakeResponseBuilder {
        private String platformVersion;
        private Object ident;

        private HandshakeResponseBuilder() {
        }

        public HandshakeResponseBuilder withPlatformVersion(final String platformVersion) {
            this.platformVersion = platformVersion;
            return this;
        }

        public HandshakeResponseBuilder withIdent(final Object ident) {
            this.ident = ident;
            return this;
        }

        public HandshakeResponse build() {
            return new HandshakeResponse(platformVersion, ident);
        }
    }
}
