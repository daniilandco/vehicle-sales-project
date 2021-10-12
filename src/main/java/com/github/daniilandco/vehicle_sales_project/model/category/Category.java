package com.github.daniilandco.vehicle_sales_project.model.category;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "category_name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    private Category parent;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent")
    @JsonBackReference
    private Set<Category> children;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }


    public boolean isRelative(Category requestCategory) {
        if (this == requestCategory) return true;
        if (requestCategory == null) return false;

        for (var kid : requestCategory.getChildren()) {
            if (isRelative(kid)) {
                return true;
            }
        }

        return false;
    }
}