package com.github.daniilandco.vehicle_sales_project.model.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "Cart")
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT")
    @Field(type = FieldType.Long)
    private Long id;
//
//    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
//    private List<Ad> ads = new ArrayList<>();
//
//    @OneToOne(mappedBy = "cart", cascade = CascadeType.ALL)
//    private User user;


}
