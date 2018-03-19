package com.rfksystems.intersocket.response;

import com.rfksystems.intersocket.frames.S2CMessageErrorFrame;

public class ErrorResponse {
    private final S2CMessageErrorFrame.ERROR_CODE code;
    private final String description;

    private ErrorResponse(final S2CMessageErrorFrame.ERROR_CODE code, final String description) {
        this.code = code;
        this.description = description;
    }

    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder();
    }

    public static ErrorResponse unauthorized() {
        return unauthorized("Not authorized to perform this action");
    }

    public static ErrorResponse unauthorized(final String description) {
        return builder()
                .withDescription(description)
                .withCode(S2CMessageErrorFrame.ERROR_CODE.UNAUTHORIZED)
                .build();
    }

    public static ErrorResponse exception(final Throwable throwable) {
        return builder()
                .withDescription(throwable.getMessage())
                .withCode(S2CMessageErrorFrame.ERROR_CODE.ERROR)
                .build();
    }

    public S2CMessageErrorFrame.ERROR_CODE getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static final class ErrorResponseBuilder {
        private S2CMessageErrorFrame.ERROR_CODE code;
        private String description;

        private ErrorResponseBuilder() {
        }

        public ErrorResponseBuilder withCode(final S2CMessageErrorFrame.ERROR_CODE code) {
            this.code = code;
            return this;
        }

        public ErrorResponseBuilder withDescription(final String description) {
            this.description = description;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(code, description);
        }
    }
}
