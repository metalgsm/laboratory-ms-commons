package com.laboratory.ms.base.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laboratory.ms.base.model.ErrorResponse;
import com.laboratory.ms.base.service.HttpStatusException;
import com.laboratory.ms.base.service.ObjectErrorException;

import lombok.AllArgsConstructor;

@ControllerAdvice
@AllArgsConstructor
public class DefaultControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultControllerAdvice.class);

    private ObjectMapper objectMapper;

    @ExceptionHandler
    ResponseEntity<Object> httpStatusExceptionHandler(ObjectErrorException e) {
        LOGGER.error("", e);
        return ResponseEntity.status(e.getStatusCode()).body(e.getError());
    }

    @ExceptionHandler
    ResponseEntity<ErrorResponse> httpStatusExceptionHandler(HttpStatusException e) {
        LOGGER.error("", e);
        return ResponseEntity.status(e.getStatusCode()).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler
    ResponseEntity<ErrorResponse> httpStatusCodeExceptionHandler(HttpStatusCodeException e) {
        LOGGER.error("", e);
        String body = e.getResponseBodyAsString();
        Object detail = body;
        if (objectMapper != null) {
            try {
                detail = objectMapper.readValue(body, Object.class);
            } catch (Exception e2) {
                detail = body;
            }
        }
        return ResponseEntity.status(e.getStatusCode()).body(new ErrorResponse(e.getMessage(), detail));
    }

    @ExceptionHandler
    ResponseEntity<ErrorResponse> exceptionHandler(Exception e) {
        LOGGER.error("", e);
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

}
