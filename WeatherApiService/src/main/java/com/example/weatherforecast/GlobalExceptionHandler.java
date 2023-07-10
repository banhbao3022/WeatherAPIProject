package com.example.weatherforecast;

import com.example.weatherforecast.geolocation.GeolocationException;
import com.example.weatherforecast.hourlyweather.BadRequestException;
import com.example.weatherforecast.location.LocationNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO handleGenericException(HttpServletRequest request, Exception ex) {
        logger.error(ex.getMessage(), ex);

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setTimestamp(new Date());
        errorDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorDTO.setPath(request.getServletPath());
        errorDTO.addError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        return errorDTO;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO handleBadRequestException(HttpServletRequest request, Exception ex) {
        logger.error(ex.getMessage(), ex);

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setTimestamp(new Date());
        errorDTO.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDTO.setPath(request.getServletPath());
        errorDTO.addError(ex.getMessage());
        return errorDTO;

    }

    @ExceptionHandler({LocationNotFoundException.class, GeolocationException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorDTO handleNotFoundException(HttpServletRequest request, Exception ex) {
        logger.error(ex.getMessage(), ex);

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setTimestamp(new Date());
        errorDTO.setStatus(HttpStatus.NOT_FOUND.value());
        errorDTO.setPath(request.getServletPath());
        errorDTO.addError(ex.getMessage());
        return errorDTO;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO handleConstraintViolationException(HttpServletRequest request, Exception ex) {
        logger.error(ex.getMessage(), ex);
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setTimestamp(new Date());
        errorDTO.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDTO.setPath(request.getServletPath());
        var constraintViolationSet = ((ConstraintViolationException) ex).getConstraintViolations();
        constraintViolationSet.forEach(constraintViolation -> errorDTO.addError(constraintViolation.getPropertyPath() + ":"+ constraintViolation.getMessage()));
        return errorDTO;
    }
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.error(ex.getMessage(), ex);
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setTimestamp(new Date());
        errorDTO.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDTO.setPath(((ServletWebRequest) request).getRequest().getServletPath());
        var errors = ex.getFieldErrors();
        errors.forEach(fieldError -> errorDTO.addError(fieldError.getDefaultMessage()));
        return new ResponseEntity<>(errorDTO, headers, status);
    }
}
