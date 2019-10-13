package com.laboratory.ms.base.service;

public class NotFoundException extends HttpStatusException {

    private static final long serialVersionUID = 5822417085271784230L;

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }

}
