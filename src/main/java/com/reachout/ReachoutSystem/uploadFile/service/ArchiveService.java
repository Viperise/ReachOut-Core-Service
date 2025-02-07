package com.reachout.ReachoutSystem.uploadFile.service;

import com.reachout.ReachoutSystem.advertisement.entity.Archive;
import com.reachout.ReachoutSystem.advertisement.repository.ArchiveRepository;
import com.reachout.ReachoutSystem.uploadFile.entity.ArchiveContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class ArchiveService {
    @Autowired
    private ArchiveRepository archiveRepository;

    public Archive saveFile(String base64File, String fileName, String fileType, ArchiveContext context) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64File);
            InputStream fileStream = new ByteArrayInputStream(decodedBytes);

            if (FirebaseApp.getApps().isEmpty()) {
                throw new IllegalStateException("FirebaseApp não foi inicializado");
            }

            String filePath = context.getFolder() + "/" + fileName;
            StorageClient.getInstance().bucket().create(filePath, fileStream, fileType);

            String publicUrl = String.format("https://firebasestorage.googleapis.com/v0/b/reachout-2.appspot/o/%s?alt=media",
                    StorageClient.getInstance().bucket().getName(), filePath);

            Archive archive = new Archive();
            archive.setPathName(publicUrl);
            archive.setName(fileName);
            archive.setType(fileType);
            archive.setCreatedAt(LocalDateTime.now());
            archive.setUpdatedAt(LocalDateTime.now());

            return archiveRepository.save(archive);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar arquivo.");
        }
    }
}
