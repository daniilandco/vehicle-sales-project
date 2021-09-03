package com.github.daniilandco.vehicle_sales_project.model.category;

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

    @ManyToOne(fetch = FetchType.LAZY)
    private Category parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private Set<Category> children;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }
}