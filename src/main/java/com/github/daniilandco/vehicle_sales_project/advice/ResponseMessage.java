package com.github.daniilandco.vehicle_sales_project.advice;

import lombok.AllArgsConstructor;

/**
 * Enum with set of strings for exception titles.
 *
 * @author com.github.daniilandco
 * @version 1.0
 */
@AllArgsConstructor
public enum ResponseMessage {
    USER_IS_NOT_LOGGED_IN_ERROR("user is not logged in error"),
    AD_DOES_NOT_BELONG_TO_LOGGED_IN_USER_ERROR("ad does not belong to logged-in user error"),
    AD_NOT_FOUND_ERROR("ad not found error"),
    DATABASE_TRANSACTION_ERROR("database transaction error"),
    INVALID_DATA_FORMAT_ERROR("invalid data format error");

    public final String message;
}