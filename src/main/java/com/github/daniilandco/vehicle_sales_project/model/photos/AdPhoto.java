package com.github.daniilandco.vehicle_sales_project.model.photos;

import com.github.daniilandco.vehicle_sales_project.model.ad.Ad;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "AdPhoto") // This tells Hibernate to make a table out of this class
@Table(name = "AdPhoto")
public class AdPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    @JoinColumn(name = "ad_id")
    private Ad ad;

    @Column(name = "photo")
    private String photo;

    public AdPhoto() {
    }

    public AdPhoto(Ad ad, String photo) {
        this.ad = ad;
        this.photo = photo;
    }
}
