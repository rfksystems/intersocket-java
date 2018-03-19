package com.rfksystems.intersocket.frames;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rfksystems.intersocket.MessageType;

public class C2SHandshakeFrame {
    @JsonProperty("protocol")
    private final short protocolVersion;

    @JsonCreator
    C2SHandshakeFrame(
            @JsonProperty("protocol") final short protocolVersion
    ) {
        this.protocolVersion = protocolVersion;
    }

    @JsonIgnore
    public short getProtocolVersion() {
        return protocolVersion;
    }

    @Override
    public String toString() {
        return String.format("%s{protocol=%s}", MessageType.C2S_HANDSHAKE, protocolVersion);
    }
}
