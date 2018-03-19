package com.rfksystems.intersocket.frames;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.rfksystems.intersocket.MessageType;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class S2CErrorFrame {
    @JsonProperty("code")
    private final ERROR_CODE code;

    @JsonProperty("appCode")
    private final int appCode;

    @JsonProperty("reference")
    private final String reference;

    @JsonProperty("description")
    private final String description;

    @JsonProperty("data")
    private final JsonNode data;

    private S2CErrorFrame(
            final ERROR_CODE code,
            final int appCode,
            final String reference,
            final String description,
            final JsonNode data
    ) {
        this.code = code;
        this.appCode = appCode;
        this.reference = reference;
        this.description = description;
        this.data = data;
    }

    public static S2CMessageErrorFrameBuilder builder() {
        return new S2CMessageErrorFrameBuilder();
    }

    @JsonIgnore
    public ERROR_CODE getCode() {
        return code;
    }

    @JsonIgnore
    public int getAppCode() {
        return appCode;
    }

    @JsonIgnore
    public String getReference() {
        return reference;
    }

    @JsonIgnore
    public String getDescription() {
        return description;
    }

    @JsonIgnore
    public JsonNode getData() {
        return data;
    }

    @Override
    public String toString() {
        return String.format(
                "%s{code=%s, appCode=%d, reference='%s', description='%s', data=[TRUNCATED]}",
                MessageType.S2C_ERROR,
                code,
                appCode,
                reference,
                description
        );
    }

    public enum ERROR_CODE {
        PARSE_ERROR {
            @Override
            int toIntCode() {
                return 0;
            }
        },
        CUSTOM {
            @Override
            int toIntCode() {
                return 1;
            }
        },
        PROTOCOL_MISMATCH {
            @Override
            int toIntCode() {
                return 2;
            }
        };

        abstract int toIntCode();
    }

    public static final class S2CMessageErrorFrameBuilder {
        private int appCode;
        private String reference;
        private String description;
        private JsonNode data;
        private ERROR_CODE code;

        private S2CMessageErrorFrameBuilder() {
        }

        public S2CMessageErrorFrameBuilder withCode(final ERROR_CODE code) {
            this.code = code;
            return this;
        }

        public S2CMessageErrorFrameBuilder withAppCode(final int appCode) {
            this.appCode = appCode;
            return this;
        }

        public S2CMessageErrorFrameBuilder withReference(final String reference) {
            this.reference = reference;
            return this;
        }

        public S2CMessageErrorFrameBuilder withDescription(final String description) {
            this.description = description;
            return this;
        }

        public S2CMessageErrorFrameBuilder withData(final JsonNode data) {
            this.data = data;
            return this;
        }

        public S2CErrorFrame build() {
            return new S2CErrorFrame(code, appCode, reference, description, data);
        }
    }
}
