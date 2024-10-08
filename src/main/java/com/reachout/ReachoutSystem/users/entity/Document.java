package com.reachout.ReachoutSystem.users.entity;

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
@Table(name = "TAB_DOCUMENTS")
public class Document {
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "seq_documents", sequenceName = "SEQ_DOCUMENTS", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_documents")
    private Integer id;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    @Column(name = "document_number")
    private String documentNumber;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}

