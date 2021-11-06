package com.github.daniilandco.vehicle_sales_project.service.mail;

public interface MailService {
    void send(String emailTo, String subject, String message);
}
