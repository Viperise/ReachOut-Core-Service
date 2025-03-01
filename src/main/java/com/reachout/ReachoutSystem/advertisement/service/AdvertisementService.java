package com.reachout.ReachoutSystem.advertisement.service;

import com.google.firebase.cloud.StorageClient;
import com.reachout.ReachoutSystem.advertisement.dto.*;
import com.reachout.ReachoutSystem.advertisement.entity.Advertisement;
import com.reachout.ReachoutSystem.advertisement.entity.Archive;
import com.reachout.ReachoutSystem.advertisement.repository.AdvertisementRepository;
import com.reachout.ReachoutSystem.advertisement.repository.ArchiveRepository;
import com.reachout.ReachoutSystem.advertisement.resources.FirebaseStorageService;
import com.reachout.ReachoutSystem.establishment.repository.EstablishmentRepository;
import com.reachout.ReachoutSystem.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final ArchiveRepository archiveRepository;
    private final ArchiveService archiveService;
    private final EstablishmentRepository establishmentRepository;
    private final UserRepository userRepository;
    private final FirebaseStorageService firebaseStorageService;

    public Page<AdvertisementListDTO> findAll(Pageable pageable) {
        return advertisementRepository.findAll(pageable).map(this::convertToDTO);
    }

    public List<AdvertisementListDTO> findAllNonPaged() {
        return advertisementRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Advertisement createAdvertisement(AdvertisementCreateDTO dto) {
        Advertisement advertisement = new Advertisement();
        advertisement.setName(dto.getName());
        advertisement.setDescription(dto.getDescription());
        advertisement.setStatus(dto.getStatus());
        advertisement.setCreatedAt(LocalDateTime.now());
        advertisement.setUpdatedAt(LocalDateTime.now());

        advertisement.setEstablishment(establishmentRepository.findById(Long.valueOf(dto.getEstablishmentId())).orElseThrow());
        advertisement.setUser(userRepository.findById(Long.valueOf(dto.getUserId())).orElseThrow());

        Archive archive = archiveService.saveFile(dto);
        advertisement.setArchive(archive);

        return advertisementRepository.save(advertisement);
    }

    public Archive saveFile(AdvertisementCreateDTO dto) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(dto.getFileBase64());
            InputStream fileStream = new ByteArrayInputStream(decodedBytes);

            String filePath = "advertisements/" + dto.getFileName();
            StorageClient.getInstance().bucket().create(filePath, fileStream, dto.getFileType());

            String publicUrl = String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
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

    @Transactional
    public Advertisement editAdvertisement(AdvertisementEditDTO dto) {
        Optional<Advertisement> advertisementOpt = advertisementRepository.findById(Long.valueOf(dto.getId()));
        if (advertisementOpt.isPresent()) {
            Advertisement advertisement = advertisementOpt.get();

            advertisement.setName(dto.getName());
            advertisement.setDescription(dto.getDescription());
            advertisement.setStatus(dto.getStatus());

            advertisement.setUpdatedAt(LocalDateTime.now());

            return advertisementRepository.save(advertisement);
        } else {
            throw new IllegalArgumentException("Anúncio não encontrado com o ID: " + dto.getId());
        }
    }


    @Transactional
    public AdvertisementResponseDTO deactivateAdvertisement(AdvertisementDeactiveDTO dto) {
        Optional<Advertisement> advertisementOpt = advertisementRepository.findById(Long.valueOf(dto.getId()));
        if (advertisementOpt.isPresent()) {
            Advertisement advertisement = advertisementOpt.get();

            advertisement.setStatus(false);
            advertisement.setUpdatedAt(LocalDateTime.now());

            Advertisement updatedAd = advertisementRepository.save(advertisement);

            AdvertisementResponseDTO responseDTO = new AdvertisementResponseDTO();

            responseDTO.setId(updatedAd.getId());
            responseDTO.setName(updatedAd.getName());
            responseDTO.setDescription(updatedAd.getDescription());
            responseDTO.setStatus(updatedAd.getStatus());
            responseDTO.setEstablishmentName(updatedAd.getEstablishment().getName());

            return responseDTO;
        } else {
            throw new IllegalArgumentException("Anúncio não encontrado com o ID: " + dto.getId());
        }
    }

    @Transactional
    public AdvertisementResponseDTO reactivateAdvertisement(AdvertisementDeactiveDTO dto) {
        Optional<Advertisement> advertisementOpt = advertisementRepository.findById(Long.valueOf(dto.getId()));
        if (advertisementOpt.isPresent()) {
            Advertisement advertisement = advertisementOpt.get();

            advertisement.setStatus(true);
            advertisement.setUpdatedAt(LocalDateTime.now());

            Advertisement updatedAd = advertisementRepository.save(advertisement);

            AdvertisementResponseDTO responseDTO = new AdvertisementResponseDTO();

            responseDTO.setId(updatedAd.getId());
            responseDTO.setName(updatedAd.getName());
            responseDTO.setDescription(updatedAd.getDescription());
            responseDTO.setStatus(updatedAd.getStatus());
            responseDTO.setEstablishmentName(updatedAd.getEstablishment().getName());

            return responseDTO;
        } else {
            throw new IllegalArgumentException("Anúncio não encontrado com o ID: " + dto.getId());
        }
    }




    private AdvertisementListDTO convertToDTO(Advertisement advertisement) {
        AdvertisementListDTO dto = new AdvertisementListDTO();
        dto.setId(advertisement.getId());
        dto.setName(advertisement.getName());
        dto.setDescription(advertisement.getDescription());
        dto.setStatus(advertisement.getStatus());
        dto.setEstablishmentName(advertisement.getEstablishment().getName());
        dto.setUserName(advertisement.getUser().getName());
        dto.setArchivePath(advertisement.getArchive().getPathName());
        dto.setCreatedAt(advertisement.getCreatedAt());
        return dto;
    }

}
