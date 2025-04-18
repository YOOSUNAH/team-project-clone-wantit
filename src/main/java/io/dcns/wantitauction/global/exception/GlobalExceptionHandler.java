package io.dcns.wantitauction.global.exception;

import io.dcns.wantitauction.global.dto.ExceptionDto;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;

import java.util.NoSuchElementException;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "ExceptionController")
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 400 Bad Request
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDto> handleBadRequestException(Exception e) {
        return createResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDto> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        return createResponse(HttpStatus.BAD_REQUEST,
                Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    // 404 Not Found
    @ExceptionHandler({
            NullPointerException.class,
            NoSuchElementException.class,
            UserNotFoundException.class,
            EntityNotFoundException.class
    })
    public ResponseEntity<ExceptionDto> handleNotFoundException(Exception e) {
        return createResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    // 500 Internal Server Error
    @ExceptionHandler({InterruptedException.class, LiveBidException.class, MessagingException.class})
    public ResponseEntity<ExceptionDto> handleInterruptedException(Exception e) {
        return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    private ResponseEntity<ExceptionDto> createResponse(HttpStatus status, String message) {
        return ExceptionDto.of(status, message);
    }
}
