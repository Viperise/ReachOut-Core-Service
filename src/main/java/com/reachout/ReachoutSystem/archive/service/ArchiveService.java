package com.reachout.ReachoutSystem.archive.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import com.reachout.ReachoutSystem.archive.entity.Archive;
import com.reachout.ReachoutSystem.archive.repository.ArchiveRepository;
import com.reachout.ReachoutSystem.archive.entity.ArchiveContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ArchiveService {
    @Autowired
    private ArchiveRepository archiveRepository;

    public Archive saveFile(String base64File, String fileName, String fileType, ArchiveContext context) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64File);
            InputStream fileStream = new ByteArrayInputStream(decodedBytes);

            if (FirebaseApp.getApps().isEmpty()) throw new IllegalStateException("FirebaseApp não foi inicializado");

            String filePath = context.getFolder() + "/" + fileName;
            StorageClient.getInstance().bucket().create(filePath, fileStream, fileType);

            String publicUrl = String.format(
                    "https://firebasestorage.googleapis.com/v0/b/reachout-2.appspot.com/o/%s?alt=media",
                    URLEncoder.encode(filePath, StandardCharsets.UTF_8)
            );


            Archive archive = new Archive();
            archive.setPathName(publicUrl);
            archive.setName(fileName);
            archive.setType(fileType);
            archive.setContext(String.valueOf(context));
            archive.setCreatedAt(LocalDateTime.now());
            archive.setUpdatedAt(LocalDateTime.now());

            return archiveRepository.save(archive);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar arquivo.");
        }
    }
}
