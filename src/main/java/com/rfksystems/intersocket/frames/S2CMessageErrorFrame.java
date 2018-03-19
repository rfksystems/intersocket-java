package com.rfksystems.intersocket.frames;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rfksystems.intersocket.MessageType;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class S2CMessageErrorFrame {
    @JsonProperty("code")
    private final ERROR_CODE code;

    @JsonProperty("id")
    private final String id;

    @JsonProperty("description")
    private final String description;

    private S2CMessageErrorFrame(
            final ERROR_CODE code,
            final String id,
            final String description
    ) {
        this.code = code;
        this.id = id;
        this.description = description;
    }

    public static S2CMessageErrorFrameBuilder builder() {
        return new S2CMessageErrorFrameBuilder();
    }

    public static S2CMessageErrorFrame unhandled(final String id, final Throwable throwable) {
        return builder()
                .withId(id)
                .withCode(ERROR_CODE.UNHANDLED_ERROR)
                .withDescription(throwable.getMessage())
                .build();
    }

    @JsonIgnore
    public ERROR_CODE getCode() {
        return code;
    }

    @JsonIgnore
    public String getId() {
        return id;
    }

    @JsonIgnore
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format(
                "%s{code=%s, id='%s', description='%s'}",
                MessageType.S2C_MESSAGE_ERROR,
                code,
                id,
                description
        );
    }

    public enum ERROR_CODE {
        UNHANDLED_ERROR {
            @Override
            int toIntCode() {
                return 0;
            }
        },
        UNAUTHORIZED {
            @Override
            int toIntCode() {
                return 1;
            }
        }, ERROR {
            @Override
            int toIntCode() {
                return 2;
            }
        };

        abstract int toIntCode();
    }

    public static final class S2CMessageErrorFrameBuilder {
        private ERROR_CODE code;
        private String id;
        private String description;

        private S2CMessageErrorFrameBuilder() {
        }

        public S2CMessageErrorFrameBuilder withId(final String id) {
            this.id = id;
            return this;
        }

        public S2CMessageErrorFrameBuilder withDescription(final String description) {
            this.description = description;
            return this;
        }

        public S2CMessageErrorFrameBuilder withCode(final ERROR_CODE code) {
            this.code = code;
            return this;
        }


        public S2CMessageErrorFrame build() {
            return new S2CMessageErrorFrame(code, id, description);
        }
    }
}
