package com.reachout.ReachoutSystem.advertisement.entity;

import com.reachout.ReachoutSystem.establishment.entity.Establishment;
import com.reachout.ReachoutSystem.user.entity.User;
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
@Table(name = "TAB_ADVERTISEMENT")
public class Advertisement {
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "seq_advertisements", sequenceName = "SEQ_ADVERTISEMENTS", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_advertisements")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "STATUS")
    private Boolean status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tab_advertisement_establishments",
            joinColumns = @JoinColumn(name = "advertisement_id"),
            inverseJoinColumns = @JoinColumn(name = "establishment_id")
    )
    private List<Establishment> establishment = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", nullable = false, unique = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ARCHIVE_ID")
    private Archive archive;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}
