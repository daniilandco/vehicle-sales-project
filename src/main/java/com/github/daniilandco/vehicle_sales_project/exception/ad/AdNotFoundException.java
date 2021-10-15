package com.github.daniilandco.vehicle_sales_project.exception.ad;

public class AdNotFoundException extends Exception {
    public AdNotFoundException() {
        super();
    }

    public AdNotFoundException(String message) {
        super(message);
    }

    public AdNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
