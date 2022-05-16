package com.github.daniilandco.vehicle_sales_project.advice;

import com.github.daniilandco.vehicle_sales_project.dto.model.ErrorDTO;
import com.github.daniilandco.vehicle_sales_project.exception.ad.AdDoesNotBelongToLoggedInUserException;
import com.github.daniilandco.vehicle_sales_project.exception.ad.AdNotFoundException;
import com.github.daniilandco.vehicle_sales_project.exception.auth.EmailAlreadyExistsException;
import com.github.daniilandco.vehicle_sales_project.exception.auth.UserIsNotLoggedInException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = UserIsNotLoggedInException.class)
    public ResponseEntity<ErrorDTO> handler(final UserIsNotLoggedInException exception) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorDTO(
                        HttpStatus.BAD_REQUEST.name(),
                        ResponseMessage.USER_IS_NOT_LOGGED_IN_ERROR.message,
                        exception.getMessage())
                );
    }

    @ExceptionHandler(value = AdDoesNotBelongToLoggedInUserException.class)
    public ResponseEntity<ErrorDTO> handler(final AdDoesNotBelongToLoggedInUserException exception) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorDTO(
                        HttpStatus.BAD_REQUEST.name(),
                        ResponseMessage.AD_DOES_NOT_BELONG_TO_LOGGED_IN_USER_ERROR.message,
                        exception.getMessage())
                );
    }

    @ExceptionHandler(value = AdNotFoundException.class)
    public ResponseEntity<ErrorDTO> handler(final AdNotFoundException exception) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorDTO(
                        HttpStatus.BAD_REQUEST.name(),
                        ResponseMessage.AD_NOT_FOUND_ERROR.message,
                        exception.getMessage())
                );
    }

    @ExceptionHandler(value = EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorDTO> handler(final EmailAlreadyExistsException exception) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorDTO(
                        HttpStatus.BAD_REQUEST.name(),
                        ResponseMessage.AD_NOT_FOUND_ERROR.message,
                        exception.getMessage())
                );
    }
}
