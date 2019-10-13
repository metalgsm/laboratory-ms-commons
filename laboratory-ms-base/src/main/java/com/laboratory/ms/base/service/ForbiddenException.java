package com.laboratory.ms.base.service;

public class ForbiddenException extends HttpStatusException {

    private static final long serialVersionUID = -1521306755266534091L;

    public ForbiddenException() {
    }

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(Throwable cause) {
        super(cause);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return 403;
    }

}
