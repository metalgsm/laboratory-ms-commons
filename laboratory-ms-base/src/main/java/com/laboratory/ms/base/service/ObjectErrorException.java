package com.laboratory.ms.base.service;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = false, of = {"statusCode", "error"})
public class ObjectErrorException extends HttpStatusException {

    private static final long serialVersionUID = 6758108727008960148L;

    @Getter
    private int statusCode;

    @Getter
    private Object error;

    public ObjectErrorException(int statusCode, Object error) {
        this.statusCode = statusCode;
        this.error = error;
    }

    public ObjectErrorException(int statusCode, Object error, String message) {
        super(message);
        this.statusCode = statusCode;
        this.error = error;
    }

    public ObjectErrorException(int statusCode, Object error, Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
        this.error = error;
    }

    public ObjectErrorException(int statusCode, Object error, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.error = error;
    }

}
