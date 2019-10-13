package com.laboratory.ms.base.service;

public class BadRequestException extends HttpStatusException {

    private static final long serialVersionUID = 6758108727008960148L;

    public BadRequestException() {
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }

}
