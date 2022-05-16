package com.github.daniilandco.vehicle_sales_project.exception.auth;

public class UnsuccessfulActivationException extends Exception {
    public UnsuccessfulActivationException(String message) {
        super(message);
    }

    public UnsuccessfulActivationException() {
        super();
    }
}
