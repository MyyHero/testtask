package com.example.testtask.exception.handler;

import com.example.testtask.dto.exception.ErrorResponse;
import com.example.testtask.exception.AccessDeniedException;
import com.example.testtask.exception.AccountNotFoundException;
import com.example.testtask.exception.EmailAlreadyExistsException;
import com.example.testtask.exception.EmailNotFoundException;
import com.example.testtask.exception.InsufficientFundsException;
import com.example.testtask.exception.InvalidAuthenticationException;
import com.example.testtask.exception.InvalidEmailOrPasswordException;
import com.example.testtask.exception.InvalidNumberOfEmails;
import com.example.testtask.exception.InvalidNumberOfPhones;
import com.example.testtask.exception.InvalidPhoneOrPasswordException;
import com.example.testtask.exception.InvalidTransferAmountException;
import com.example.testtask.exception.PhoneNumberAlreadyExistsException;
import com.example.testtask.exception.PhoneNumberNotFoundException;
import com.example.testtask.exception.SelfTransferException;
import com.example.testtask.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            InvalidTransferAmountException.class,
            InvalidPhoneOrPasswordException.class,
            InvalidEmailOrPasswordException.class,
            SelfTransferException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(
            RuntimeException ex, HttpServletRequest req) {

        return build(HttpStatus.BAD_REQUEST, ex, req);
    }


    @ExceptionHandler(InvalidAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(
            InvalidAuthenticationException ex, HttpServletRequest req) {

        return build(HttpStatus.UNAUTHORIZED, ex, req);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(
            AccessDeniedException ex, HttpServletRequest req) {

        return build(HttpStatus.FORBIDDEN, ex, req);
    }


    @ExceptionHandler({
            UserNotFoundException.class,
            AccountNotFoundException.class,
            EmailNotFoundException.class,
            PhoneNumberNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(
            RuntimeException ex, HttpServletRequest req) {

        return build(HttpStatus.NOT_FOUND, ex, req);
    }


    @ExceptionHandler({
            EmailAlreadyExistsException.class,
            PhoneNumberAlreadyExistsException.class,
            InvalidNumberOfEmails.class,
            InvalidNumberOfPhones.class,
            InsufficientFundsException.class
    })
    public ResponseEntity<ErrorResponse> handleConflict(
            RuntimeException ex, HttpServletRequest req) {

        return build(HttpStatus.CONFLICT, ex, req);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest req) {

        String msg = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::fieldErr)
                .collect(Collectors.joining("; "));

        return build(HttpStatus.BAD_REQUEST,
                new RuntimeException(msg), req);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(
            Exception ex, HttpServletRequest req) {

        log.error("Необработанное исключение", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex, req);
    }


    private ResponseEntity<ErrorResponse> build(HttpStatus status,
                                                Exception ex,
                                                HttpServletRequest req) {

        ErrorResponse body = ErrorResponse.of(
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                req.getRequestURI()
        );
        return new ResponseEntity<>(body, status);
    }

    private String fieldErr(FieldError fe) {
        return "%s %s".formatted(fe.getField(), fe.getDefaultMessage());
    }
}
