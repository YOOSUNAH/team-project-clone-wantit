package io.dcns.wantitauction.global.exception;

import io.dcns.wantitauction.global.dto.ExceptionDto;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDto> handBadRequestException(Exception e) {
        return createResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({NullPointerException.class, NoSuchElementException.class,
        UserNotFoundException.class})
    public ResponseEntity<ExceptionDto> handleNotFoundException(Exception e) {
        return createResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDto> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e
    ) {
        return createResponse(HttpStatus.BAD_REQUEST,
            e.getBindingResult().getFieldError().getDefaultMessage());
    }

    private ResponseEntity<ExceptionDto> createResponse(HttpStatus status, String message) {
        return ExceptionDto.of(status, message);
    }

}
