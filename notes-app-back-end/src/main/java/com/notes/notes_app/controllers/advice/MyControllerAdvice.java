package com.notes.notes_app.controllers.advice;

import com.notes.notes_app.exceptions.CustomException;
import com.notes.notes_app.exceptions.CustomExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//For custom errors
@ControllerAdvice

@CrossOrigin
public class MyControllerAdvice {
    // Custom errors
    // @SuppressWarnings({ "rawtypes", "unchecked" })
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    // @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> userCustomException(CustomException ex) {
        return new ResponseEntity(CustomExceptionDto.builder()
                .message(ex.getMessage())
                .httpStatus(ex.getHttpStatus())
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // @SuppressWarnings({ "rawtypes", "unchecked" })
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> standardException(RuntimeException ex) {
        return new ResponseEntity(CustomExceptionDto.builder()
                .message(ex.getMessage())
                .httpStatus(ex.toString())
                .build(), HttpStatus.NOT_FOUND);
    }
}
