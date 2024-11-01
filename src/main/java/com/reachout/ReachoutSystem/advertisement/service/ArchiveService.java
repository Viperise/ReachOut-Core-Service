package com.reachout.ReachoutSystem.advertisement.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import com.reachout.ReachoutSystem.advertisement.dto.AdvertisementCreateDTO;
import com.reachout.ReachoutSystem.advertisement.entity.Archive;
import com.reachout.ReachoutSystem.advertisement.repository.ArchiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ArchiveService {
    @Autowired
    private ArchiveRepository archiveRepository;

    public Archive saveFile(AdvertisementCreateDTO dto) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(dto.getFileBase64());
            InputStream fileStream = new ByteArrayInputStream(decodedBytes);

            if (FirebaseApp.getApps().isEmpty()) {
                throw new IllegalStateException("FirebaseApp não foi inicializado");
            }

            String filePath = "advertisements/" + dto.getFileName();
            StorageClient.getInstance().bucket().create(filePath, fileStream, dto.getFileType());

            String publicUrl = String.format("https://firebasestorage.googleapis.com/v0/b/reachout-2.appspot/o/%s?alt=media",
                    StorageClient.getInstance().bucket().getName(), filePath);

            Archive archive = new Archive();
            archive.setPathName(publicUrl);
            archive.setName(dto.getFileName());
            archive.setType(dto.getFileType());
            archive.setCreatedAt(LocalDateTime.now());
            archive.setUpdatedAt(LocalDateTime.now());

            return archiveRepository.save(archive);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar arquivo.");
        }
    }
}
