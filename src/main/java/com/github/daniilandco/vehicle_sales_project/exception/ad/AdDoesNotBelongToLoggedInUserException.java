package com.github.daniilandco.vehicle_sales_project.exception.ad;

public class AdDoesNotBelongToLoggedInUserException extends Exception {
    public AdDoesNotBelongToLoggedInUserException() {
        super();
    }

    public AdDoesNotBelongToLoggedInUserException(String message) {
        super(message);
    }

    public AdDoesNotBelongToLoggedInUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
