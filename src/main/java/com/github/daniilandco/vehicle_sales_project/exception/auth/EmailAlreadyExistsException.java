package com.github.daniilandco.vehicle_sales_project.exception.auth;

public class EmailAlreadyExistsException extends Exception {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }

    public EmailAlreadyExistsException() {
        super();
    }
}
