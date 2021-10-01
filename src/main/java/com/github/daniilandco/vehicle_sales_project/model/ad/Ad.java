package com.github.daniilandco.vehicle_sales_project.model.ad;

import com.github.daniilandco.vehicle_sales_project.model.category.Category;
import com.github.daniilandco.vehicle_sales_project.model.photos.AdPhoto;
import com.github.daniilandco.vehicle_sales_project.model.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@Entity(name = "Ad") // This tells Hibernate to make a table out of this class
@Table(name = "Ad")
@Indexed
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    @JoinColumn(name = "author_id")
    private User author;

    @Column(name = "title")
    @Field
    private String title;

    @Column(name = "description")
    @Field
    private String description;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    @JoinColumn(name = "category_id")
    @ContainedIn
    private Category category;

    @Column(name = "price")
    @Field
    @NumericField
    private BigDecimal price;

    @Column(name = "release_year")
    @Field
    @DateBridge(resolution = Resolution.MONTH)
    private Date releaseYear;

    @Column(name = "created_at")
    @CreationTimestamp
    @Field(name = "created_at", store = Store.NO)
    @SortableField(forField = "created_at")
    private Timestamp createdAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<AdPhoto> photos;


    public Ad() {
    }

    public Ad(User author, String title, String description, Category category, BigDecimal price, Date releaseYear, Status status) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.category = category;
        this.price = price;
        this.releaseYear = releaseYear;
        this.status = status;
    }
}
