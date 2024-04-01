package io.dcns.wantitauction.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
public class ExceptionDto {

    private String message;

    public static ResponseEntity<ExceptionDto> of(
        HttpStatus status, String message
    ) {
        return ResponseEntity.status(status).body(new ExceptionDto(message));
    }
}
