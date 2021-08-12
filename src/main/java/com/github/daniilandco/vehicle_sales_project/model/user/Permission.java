package com.github.daniilandco.vehicle_sales_project.model.user;

public enum Permission {
    MANAGE("manage"),   // These permissions are temp. so far : in progress
    USE("use");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}

