package com.ylinor.client.render.model;

public class ModelParseException extends RuntimeException {
    private static final long serialVersionUID = 4767099200524103931L;

    public ModelParseException() {
        super();
    }

    public ModelParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ModelParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelParseException(String message) {
        super(message);
    }

    public ModelParseException(Throwable cause) {
        super(cause);
    }
}
