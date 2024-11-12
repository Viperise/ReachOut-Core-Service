package com.reachout.ReachoutSystem.user.service;

import com.reachout.ReachoutSystem.user.dto.DocumentEditRequestDTO;
import com.reachout.ReachoutSystem.user.entity.Document;
import com.reachout.ReachoutSystem.user.repository.DocumentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DocumentService {
    @Autowired
    private final DocumentRepository documentRepository;

    // ATUALIZA UM DOCUMENTO
    @Transactional
    public Document update(DocumentEditRequestDTO documentDTO, Long documentID) throws Exception {
        System.out.println(documentID);

        Document document = documentRepository.findById(documentID)
                .orElseThrow(() -> new EntityNotFoundException("Documento não encontrado"));

        document.setDocumentType(documentDTO.getDocumentType());
        document.setDocumentNumber(documentDTO.getDocumentNumber());
        document.setUpdatedAt(LocalDateTime.now());

        return documentRepository.save(document);
    }
}
