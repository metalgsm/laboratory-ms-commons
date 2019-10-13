package com.laboratory.ms.base.service;

public class UnauthorizedException extends HttpStatusException {

    private static final long serialVersionUID = 1149981912675461506L;

    public UnauthorizedException() {
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(Throwable cause) {
        super(cause);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }

}
