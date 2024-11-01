package com.reachout.ReachoutSystem.advertisement.resources;

import org.springframework.stereotype.Service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FirebaseStorageService {

    private final Storage storage;

    @Value("gs://reachout-2.appspot.com")
    private String bucketName;

    public FirebaseStorageService() throws Exception {
        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    public String uploadFile(MultipartFile file) {
        try {
            String uniqueFileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
            Bucket bucket = storage.get(bucketName);
            Blob blob = bucket.create(uniqueFileName, file.getBytes(), file.getContentType());
            return blob.getMediaLink();
        } catch (IOException e) {
            throw new RuntimeException("Falha ao fazer upload do arquivo", e);
        }
    }

    }

