package com.github.daniilandco.vehicle_sales_project.database_access.user.user_model;

public enum Permission {
    MANAGE("admins:manage"),   // These permissions are temp. so far : in progress
    USE("users:use");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
