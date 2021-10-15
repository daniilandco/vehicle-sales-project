package com.github.daniilandco.vehicle_sales_project.exception.auth;

public class PhoneNumberAlreadyExistsException extends Exception {
    public PhoneNumberAlreadyExistsException(String message) {
        super(message);
    }

    public PhoneNumberAlreadyExistsException() {
        super();
    }
}
