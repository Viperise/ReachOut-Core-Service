package com.reachout.ReachoutSystem.establishment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TAB_PRODUCTS")
public class Product {
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "seq_products", sequenceName = "SEQ_USERS", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_products")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PRICE")
    private Long price;

    @Column(name = "AVAILABLE")
    private Boolean available;

    @Column(name = "PHOTO_PATH")
    private String photoPath;
}
