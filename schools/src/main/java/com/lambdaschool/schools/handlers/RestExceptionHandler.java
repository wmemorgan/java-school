package com.lambdaschool.schools.handlers;

import com.lambdaschool.schools.exceptions.ResourceFoundException;
import com.lambdaschool.schools.exceptions.ResourceNotFoundException;
import com.lambdaschool.schools.models.ErrorDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private HelpFunctions helper;

    public RestExceptionHandler() {
        super();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException rnfe) {

        ErrorDetail errorDetail = new ErrorDetail();

        errorDetail.setTitle("Resource Not Found");
        errorDetail.setStatus(HttpStatus.NOT_FOUND.value());
        errorDetail.setDetail(rnfe.getMessage());
        errorDetail.setTimestamp(new Date());
        errorDetail.setDeveloperMessage(rnfe.getClass().getName());
        errorDetail.setErrors(helper.getConstraintViolation(rnfe));

        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceFoundException.class)
    public ResponseEntity<?> handleResourceFoundException(ResourceFoundException rfe) {

        ErrorDetail errorDetail = new ErrorDetail();

        errorDetail.setTitle("Resource Found");
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetail.setDetail(rfe.getMessage());
        errorDetail.setTimestamp(new Date());
        errorDetail.setDeveloperMessage(rfe.getClass().getName());
        errorDetail.setErrors(helper.getConstraintViolation(rfe));

        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ErrorDetail errorDetail = new ErrorDetail();

        errorDetail.setTitle("REST Internal Exception");
        errorDetail.setStatus(status.value());
        errorDetail.setDetail(ex.getMessage());
        errorDetail.setTimestamp(new Date());
        errorDetail.setDeveloperMessage(ex.getClass().getName());
        errorDetail.setErrors(helper.getConstraintViolation(ex));

        return new ResponseEntity<>(errorDetail, null, status);
    }
}
