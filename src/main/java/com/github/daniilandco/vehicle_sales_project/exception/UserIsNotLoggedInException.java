package com.github.daniilandco.vehicle_sales_project.exception;

public class UserIsNotLoggedInException extends Exception {
    public UserIsNotLoggedInException() {
        super();
    }

    public UserIsNotLoggedInException(String message) {
        super(message);
    }

    public UserIsNotLoggedInException(String message, Throwable cause) {
        super(message, cause);
    }
}
