package com.kentchiu.spring.base.web;


import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.kentchiu.spring.base.domain.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractRestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


    private Logger logger = LoggerFactory.getLogger(AbstractRestResponseEntityExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        RestError error = Validators.toRestError(ex);
        return handleExceptionInternal(error, ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        RestError restError = new RestError();
        restError.setCode(ex.getClass().getSimpleName());
        restError.setContent(ex.getMessage());
        return handleExceptionInternal(restError, ex, headers, status, request);
    }

    protected ResponseEntity<Object> handleExceptionInternal(RestError error, Exception ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String json = DomainUtil.toJson(error);
        logger.error("controller exception", ex);
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute("javax.servlet.error.exception", ex, WebRequest.SCOPE_REQUEST);
        }
        return new ResponseEntity<>(json, headers, status);
    }

    @ExceptionHandler({CodeBaseException.class})
    protected ResponseEntity<Object> handleCodeBaseException(final CodeBaseException ex, final WebRequest request) {
        logger.error("CodeBaseException", ex);
        HttpStatus status = ex.getStatus() == null ? HttpStatus.BAD_REQUEST : ex.getStatus();
        RestError error = new RestError(status.value(), ex.getClass().getSimpleName(), ex.getMessage());
        error.setContent(Joiner.on('\n').join(ex.getContents()));
        return handleExceptionInternal(error, ex, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        RestError error = Validators.toRestError(bindingResult);
        return handleExceptionInternal(error, ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({IllegalApiUsageException.class})
    public ResponseEntity<Object> handleIllegalApiUsageException(IllegalApiUsageException ex, final WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        RestError error = new RestError(status.value(), Validators.ILLEGAL_API_USAGE, ex.getMessage());
        return handleExceptionInternal(error, ex, new HttpHeaders(), status, request);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    protected ResponseEntity<Object> handleResourceNotFoundException(final ResourceNotFoundException ex, final WebRequest request) {
        logger.error("404 Status Code", ex);
        HttpStatus status = HttpStatus.NOT_FOUND;
        RestError error = new RestError(status.value(), Validators.RESOURCE_NOT_FOUND, "Resource Not Found");
        String msg = StringUtils.isNotBlank(ex.getMessage()) ? ex.getMessage() : "";
        error.setContent(msg);
        return handleExceptionInternal(error, ex, new HttpHeaders(), status, request);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex, final WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        RestError error = new RestError(status.value(), ex.getClass().getSimpleName(), ex.getMessage());
        return handleExceptionInternal(error, ex, new HttpHeaders(), status, request);
    }

    @ExceptionHandler({NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Object> handleInternalServerError(final RuntimeException ex, final WebRequest request) {
        logger.error("500 Status Code", ex);
        String msg = StringUtils.isNotBlank(ex.getMessage()) ? ex.getMessage() : "";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        RestError error = new RestError(status.value(), Validators.UNKNOWN, "Unknown internal server error");
        error.setContent(msg);
        return handleExceptionInternal(error, ex, new HttpHeaders(), status, request);
    }

    @ExceptionHandler({EmptyResultDataAccessException.class})
    public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, final WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        RestError error = new RestError(status.value(), ResourceNotFoundException.class.getSimpleName(), "Resource Not Found");
        return handleExceptionInternal(error, ex, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        RestError error = new RestError(HttpStatus.BAD_REQUEST.value(), Validators.DUPLICATED, "data duplicated");
        error.setContent(ex.toString());
        return this.handleExceptionInternal(error, ex, null, HttpStatus.BAD_REQUEST, request);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (StringUtils.startsWith(ex.getMessage(), "Could not read document: Failed to parse Date value ")) {
            Pattern pattern = Pattern.compile(".*format: \"(.*?)\"\\): Unparseable date: \"(.*?)\".*: (.*)\\[\"(.*?)\"]\\)");
            Matcher matcher = pattern.matcher(ex.getMessage());

            if (matcher.find()) {
                String format = matcher.group(1);
                String rejected = matcher.group(2);
                String clazz = matcher.group(3);
                String field = matcher.group(4);
                MapBindingResult result = new MapBindingResult(ImmutableMap.of(field, rejected), clazz);
                result.rejectValue(field, "typeMismatch", ex.getMessage());
                return handleBindException(new BindException(result), headers, HttpStatus.BAD_REQUEST, request);
            } else {
                IllegalApiUsageException exception = new IllegalApiUsageException(ex.getMessage());
                return this.handleIllegalApiUsageException(exception, request);
            }
        } else {
            return this.handleExceptionInternal(ex, null, headers, status, request);
        }

    }


}