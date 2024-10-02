package com.reachout.ReachoutSystem.users.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TAB_USERS")
public class User {
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "seq_users")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_users")
    private Integer id;

    @Column(name = "UID")
    private String uid;

    @Column(name = "name")
    private String name;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "STATUS")
    private Boolean status;

    @Column(name = "BIRTHDAY")
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "PROFILE_PHOTO_PATH")
    private String profilePhotoPath;

    @OneToOne
    @JoinColumn(name = "DOCUMENT_ID")
    private Document document;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}