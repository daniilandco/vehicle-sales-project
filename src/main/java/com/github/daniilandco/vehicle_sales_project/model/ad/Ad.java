package com.github.daniilandco.vehicle_sales_project.model.ad;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Year;

@Data
@Entity // This tells Hibernate to make a table out of this class
@Table(name = "Ad")
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "author_id")
    private Long authorId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "make_id")
    private Integer makeId;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "release_year")
    private Year releaseYear;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "is_active")
    private Boolean isActive;

    public Ad() {
    }
}
