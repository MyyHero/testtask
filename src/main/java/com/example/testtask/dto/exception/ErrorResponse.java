package com.example.testtask.dto.exception;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    private int status;
    private String error;
    private String message;
    private String path;


    public static ErrorResponse of(int status,
                                   String error,
                                   String message,
                                   String path) {
        return new ErrorResponse(LocalDateTime.now(), status, error, message, path);
    }
}
