package com.workshop.backgroundservice.handler;

import com.workshop.backgroundservice.dto.error.NoFieldException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorControllerAdvice {

    // INTERNAL_SERVER_ERROR handlers


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public NoFieldException onException(
            Exception e
    ) {
        return new NoFieldException(
                e.getMessage()
        );
    }

}
