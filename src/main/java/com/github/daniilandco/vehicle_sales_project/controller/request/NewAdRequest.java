package com.github.daniilandco.vehicle_sales_project.controller.request;

import com.github.daniilandco.vehicle_sales_project.config.SecurityConfig;
import com.github.daniilandco.vehicle_sales_project.model.ad.Ad;
import com.github.daniilandco.vehicle_sales_project.model.ad.Status;
import com.github.daniilandco.vehicle_sales_project.model.user.User;
import com.github.daniilandco.vehicle_sales_project.repository.user.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Data
public class NewAdRequest {
    private String title;
    private String description;
    private BigDecimal price;
    private Integer makeId;
    private Date release_year;

    @Enumerated(EnumType.STRING)
    private Status status;
}
