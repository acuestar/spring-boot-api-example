package com.api.rest.exceptions;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ExceptionsHandlers {


    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleUserNotFoundException(final UserNotFoundException e) {

        log.error("User not found thrown");
        return new ErrorResponse("USER_NOT_FOUND", "The User was not found");
    }


    @ExceptionHandler(UserMissingInformationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ErrorResponse handleContactMissingInformationException(final UserMissingInformationException e) {
        log.error("User missing information thrown");
        return new ErrorResponse("MISSING_INFORMATION", "User bad format");
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public ErrorResponse handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        log.error("Http Request Method Not Supported Exception thrown");
        return new ErrorResponse("METHOD_NOT_SUPPORTED", "Method not supported");

    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ErrorResponse handleContactMissingInformationException(final HttpMessageNotReadableException e) {
        log.error(" Http Message Not Readable Exception thrown");
        e.printStackTrace();
        return new ErrorResponse("INVALID_FORMAT", "JSON malformed");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ErrorResponse MethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error("Method Argument Not Valid Exception thrown");
        return new ErrorResponse("ARGUMENT_NOT_VALID", "User arguments invalids");
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ErrorResponse handleThrowable(final Throwable e) {
        log.error("Unexpected Error", e);
        return new ErrorResponse("INTERNAL_SERVER_ERROR", "An unexpected internal server error occurred");
    }


}
