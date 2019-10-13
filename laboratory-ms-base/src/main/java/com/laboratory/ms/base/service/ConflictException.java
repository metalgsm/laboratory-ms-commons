package com.laboratory.ms.base.service;

public class ConflictException extends HttpStatusException {

    private static final long serialVersionUID = -1720451245130370423L;

    public ConflictException() {
    }

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(Throwable cause) {
        super(cause);
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return 409;
    }

}
