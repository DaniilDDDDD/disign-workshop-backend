package com.workshop.authservice.exception;

import com.workshop.authservice.dto.error.FieldException;
import com.workshop.authservice.dto.error.NoFieldException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorHandlerControllerAdvice {


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
            JwtException.class,
            AuthenticationException.class
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


    @ExceptionHandler({
            AccessDeniedException.class
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public FieldException onAccessDeniedException(
            Exception e
    ) {
        return new FieldException(
                "Authorization",
                e.getMessage()
        );
    }


    @ExceptionHandler({
            NoSuchAlgorithmException.class,
            InvalidKeySpecException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public FieldException onAuthenticationAndAccessDeniedException(
            Exception e
    ) {
        return new FieldException(
                "Authorization",
                e.getMessage()
        );
    }


//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ResponseBody
//    public NoFieldException onException(
//            Exception e
//    ) {
//        return new NoFieldException(
//                e.getMessage()
//        );
//    }

}
