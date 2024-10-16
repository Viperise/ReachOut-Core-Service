package com.reachout.ReachoutSystem.establishment.entity;

import com.reachout.ReachoutSystem.users.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TAB_ESTABLISHMENTS")
public class Establishment {
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "seq_establishments", sequenceName = "SEQ_ESTABLISHMENTS", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_establishment")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PHOTO_PATH")
    private String photoPath;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "PHONE")
    private String phone;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private User owner;

    @Column(name = "STATUS")
    private Boolean status;

    @OneToMany(mappedBy = "establishment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}
