package com.ylinor.client.render;

public class RenderingException extends RuntimeException {

    private static final long serialVersionUID = -8888297933212225027L;

    public RenderingException() {
        super();
    }

    public RenderingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public RenderingException(String message, Throwable cause) {
        super(message, cause);
    }

    public RenderingException(String message) {
        super(message);
    }

    public RenderingException(Throwable cause) {
        super(cause);
    }

}
