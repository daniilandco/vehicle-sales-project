package com.github.daniilandco.vehicle_sales_project.model.ad;

import com.github.daniilandco.vehicle_sales_project.model.user.User;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Year;

@Data
@Entity(name = "Ad") // This tells Hibernate to make a table out of this class
@Table(name = "ad")
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private User author;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "make_id")
    private Integer makeId;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "release_year")
    private Date releaseYear;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    public Ad() {
    }

}
