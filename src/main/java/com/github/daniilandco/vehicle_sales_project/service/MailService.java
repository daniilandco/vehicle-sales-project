package com.github.daniilandco.vehicle_sales_project.service;

public interface MailService {
    void send(String emailTo, String subject, String message);
}
