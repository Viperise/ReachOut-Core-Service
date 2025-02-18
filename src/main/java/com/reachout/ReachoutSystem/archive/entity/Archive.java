package com.reachout.ReachoutSystem.archive.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TAB_ARCHIVE")
public class Archive {
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "seq_archives", sequenceName = "SEQ_ARCHIVES", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_archives")
    private Integer id;

    @Column(name = "PATHNAME")
    private String pathName;

    @Column(name = "NAME")
    private String name;

    @Column(name = "NUMBER")
    private Number size;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "CONTEXT")
    private String context;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}
