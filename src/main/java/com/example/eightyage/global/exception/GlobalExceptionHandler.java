package com.example.eightyage.global.exception;

import com.example.eightyage.global.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static com.example.eightyage.global.exception.ErrorMessage.DEFAULT_FORBIDDEN;
import static com.example.eightyage.global.exception.ErrorMessage.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HandledException.class)
    public ResponseEntity<ErrorResponse<String>> invalidRequestExceptionException(HandledException ex) {
        HttpStatus httpStatus = ex.getHttpStatus();
        return new ResponseEntity<>(ErrorResponse.of(httpStatus, ex.getMessage()), ex.getHttpStatus());
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse<List<String>> handleValidationException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        List<String> validFailedList = fieldErrors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        return ErrorResponse.of(HttpStatus.BAD_REQUEST, validFailedList);
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse<String> handleAccessDeniedException() {
        return ErrorResponse.of(HttpStatus.FORBIDDEN, DEFAULT_FORBIDDEN.getMessage());
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse<String> handleGlobalException(Exception e) {
        log.error("Exception : {}",e.getMessage(),  e);
        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.getMessage());
    }

    @ExceptionHandler(ProductImageUploadException.class)
    public ResponseEntity<String> handleProductImageUploadException(ProductImageUploadException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
