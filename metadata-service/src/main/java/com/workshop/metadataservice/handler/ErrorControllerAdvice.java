package com.workshop.metadataservice.handler;

import com.workshop.metadataservice.dto.error.FieldException;
import com.workshop.metadataservice.dto.error.NoFieldException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorControllerAdvice {


    // BAD_REQUEST handlers


    @ExceptionHandler({
            ConstraintViolationException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<FieldException> onConstraintValidationException(
            ConstraintViolationException e
    ) {
        return e.getConstraintViolations().stream()
                .map(
                        violation -> new FieldException(
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                )
                .collect(Collectors.toList());
    }


    @ExceptionHandler({
            MethodArgumentNotValidException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public FieldException onConstraintValidationException(
            MethodArgumentNotValidException e
    ) {
        return new FieldException(
                e.getParameter().getParameterName(),
                e.getMessage()
        );
    }


    @ExceptionHandler({
            JwtException.class,
            AuthenticationException.class,
            PersistenceException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public NoFieldException onUsernameNotFoundException(
            Exception e
    ) {
        return new NoFieldException(
                e.getMessage()
        );
    }


    // FORBIDDEN handlers


    @ExceptionHandler({
            AccessDeniedException.class
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public NoFieldException onAccessDeniedException(
            Exception e
    ) {
        return new NoFieldException(e.getMessage());
    }


    // INTERNAL_SERVER_ERROR handlers


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public FieldException onException(
            Exception e
    ) {
        return new FieldException(
                e.getClass().getName(),
                e.getMessage()
        );
    }

}
