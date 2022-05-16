package com.github.daniilandco.vehicle_sales_project.model.ad;

import com.github.daniilandco.vehicle_sales_project.model.category.Category;
import com.github.daniilandco.vehicle_sales_project.model.photos.AdPhoto;
import com.github.daniilandco.vehicle_sales_project.model.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Set;

@Getter
@Setter
@Entity(name = "Ad") // This tells Hibernate to make a table out of this class
@Table(name = "Ad")
@Document(indexName = Indices.AD_INDEX)
@Setting(settingPath = "static/es-settings.json")
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    @Field(type = FieldType.Long)
    private Long id;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    @JoinColumn(name = "author_id")
    @org.springframework.data.annotation.Transient
    private User author;

    @Column(name = "title")
    @Field(type = FieldType.Text)
    private String title;

    @Column(name = "description")
    @Field(type = FieldType.Text)
    private String description;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    @JoinColumn(name = "category_id")
    @org.springframework.data.annotation.Transient
    private Category category;

    @Column(name = "price")
    @Field(type = FieldType.Double)
    private BigDecimal price;

    @Column(name = "release_year")
    @Field(type = FieldType.Date)
    private Date releaseYear;

    @Column(name = "created_at")
    @CreationTimestamp
    @Field(type = FieldType.Date)
    private Date createdAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @Field(type = FieldType.Keyword)
    private Status status;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Transient
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
