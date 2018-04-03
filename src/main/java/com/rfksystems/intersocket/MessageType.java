package com.rfksystems.intersocket;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum MessageType {
    C2S_HANDSHAKE {
        @Override
        public short toShort() {
            return 10;
        }
    },
    S2C_HANDSHAKE {
        @Override
        public short toShort() {
            return 11;
        }
    },
    C2S_MESSAGE {
        @Override
        public short toShort() {
            return 12;
        }
    },
    S2C_ACK {
        @Override
        public short toShort() {
            return 13;
        }
    },
    S2C_MESSAGE {
        @Override
        public short toShort() {
            return 14;
        }
    },
    S2C_IDENT_CHANGE {
        @Override
        public short toShort() {
            return 15;
        }
    },
    S2C_PLATFORM_CHANGE {
        @Override
        public short toShort() {
            return 16;
        }
    },
    S2C_BROADCAST {
        @Override
        public short toShort() {
            return 17;
        }
    },
    S2C_ERROR {
        @Override
        public short toShort() {
            return 18;
        }
    },
    S2C_MESSAGE_ERROR {
        @Override
        public short toShort() {
            return 19;
        }
    },
    C2S_SYNCHRONIZE_TOPICS {
        @Override
        public short toShort() {
            return 20;
        }
    },
    S2C_BINARY_ATTACHMENT {
        @Override
        public short toShort() {
            return 21;
        }
    };

    public static final Map<String, MessageType> STRING_TYPE_MAP;
    public static final Map<Short, MessageType> SHORT_TYPE_MAP;

    static {
        final Map<Short, MessageType> shortMessageTypeMap = new HashMap<>();
        final Map<String, MessageType> stringMessageTypeMap = new HashMap<>();

        for (final MessageType messageType : MessageType.values()) {
            shortMessageTypeMap.put(messageType.toShort(), messageType);
            stringMessageTypeMap.put(messageType.toStringCode(), messageType);
        }

        SHORT_TYPE_MAP = Collections.unmodifiableMap(shortMessageTypeMap);
        STRING_TYPE_MAP = Collections.unmodifiableMap(stringMessageTypeMap);
    }

    public abstract short toShort();

    public String toStringCode() {
        return String.valueOf(toShort());
    }

    public String toPrefix() {
        return Intersocket.MAGIC_ID_STRING + String.valueOf(toShort());
    }
}
