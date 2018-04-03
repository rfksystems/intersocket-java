package com.rfksystems.intersocket.response;

public class ResponseWithAttachment {
    private final Object response;
    private final byte[] attachment;

    ResponseWithAttachment(final Object response, final byte[] attachment) {
        this.response = response;
        this.attachment = attachment;
    }

    public static ResponseWithAttachmentBuilder builder() {
        return new ResponseWithAttachmentBuilder();
    }

    public Object getResponse() {
        return response;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public static final class ResponseWithAttachmentBuilder {
        private Object response;
        private byte[] attachment;

        private ResponseWithAttachmentBuilder() {
        }

        public ResponseWithAttachmentBuilder withResponse(final Object response) {
            this.response = response;
            return this;
        }

        public ResponseWithAttachmentBuilder withAttachment(final byte[] attachment) {
            this.attachment = attachment;
            return this;
        }

        public ResponseWithAttachment build() {
            return new ResponseWithAttachment(response, attachment);
        }
    }
}
