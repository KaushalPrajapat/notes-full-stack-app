package com.notes.notes_app.exceptions;

import lombok.Data;

@Data
public class CustomException extends RuntimeException {
    private String httpStatus;
    public CustomException(String message, String httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}