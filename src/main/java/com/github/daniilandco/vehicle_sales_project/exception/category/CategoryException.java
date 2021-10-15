package com.github.daniilandco.vehicle_sales_project.exception.category;

public class CategoryException extends Exception {
    public CategoryException() {
        super();
    }

    public CategoryException(String message) {
        super(message);
    }

    public CategoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
