package com.backbase.blob.storage.connector.controller;

import com.backbase.buildingblocks.presentation.errors.BadRequestException;
import com.backbase.buildingblocks.presentation.errors.ConflictException;
import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.c3.storage.api.v1.model.StatusMessage;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionHandlers {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlers.class);

    private static final String VALIDATION_MSG_TEMPLATE = "Request payload did not pass schema validation: %s - %s";


    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<StatusMessage> handleValidationExceptionWithFieldErrors(
        MethodArgumentNotValidException exception) {
        LOGGER.error(exception.getMessage(), exception);
        final BindingResult result = exception.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        final String errorMessages = fieldErrors.stream().map(error ->
                String.format(VALIDATION_MSG_TEMPLATE, error.getField(), error.getDefaultMessage()))
            .collect(Collectors.joining(System.lineSeparator()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new StatusMessage().message(errorMessages));
    }

    @ExceptionHandler({InvalidFormatException.class,
        HttpMediaTypeNotSupportedException.class, BadRequestException.class})
    public ResponseEntity<StatusMessage> handleValidationException(Exception exception) {
        LOGGER.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new StatusMessage().message(exception.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<StatusMessage> handleNotFoundException(NotFoundException exception) {
        LOGGER.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new StatusMessage().message(exception.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<StatusMessage> handleConflictException(ConflictException exception) {
        LOGGER.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new StatusMessage().message(exception.getMessage()));
    }


}
