package com.reachout.ReachoutSystem.advertisement.service;

import com.reachout.ReachoutSystem.advertisement.dto.AdvertisementCreateDTO;
import com.reachout.ReachoutSystem.advertisement.dto.AdvertisementDeactivateDTO;
import com.reachout.ReachoutSystem.advertisement.dto.AdvertisementEditDTO;
import com.reachout.ReachoutSystem.advertisement.dto.AdvertisementListDTO;
import com.reachout.ReachoutSystem.advertisement.entity.Advertisement;
import com.reachout.ReachoutSystem.advertisement.repository.AdvertisementRepository;
import com.reachout.ReachoutSystem.archive.repository.ArchiveRepository;
import com.reachout.ReachoutSystem.archive.entity.Archive;
import com.reachout.ReachoutSystem.archive.resources.FirebaseStorageService;
import com.reachout.ReachoutSystem.archive.service.ArchiveService;
import com.reachout.ReachoutSystem.establishment.entity.Establishment;
import com.reachout.ReachoutSystem.establishment.repository.EstablishmentRepository;
import com.reachout.ReachoutSystem.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

        Archive archive = archiveService.saveFile(dto.getFileBase64(), dto.getFileName(),dto.getFileType(), dto.getContext());
        advertisement.setArchive(archive);

        return advertisementRepository.save(advertisement);
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
    public Advertisement deactivateAdvertisement(AdvertisementDeactivateDTO dto) {
        Optional<Advertisement> advertisementOpt = advertisementRepository.findById(Long.valueOf(dto.getId()));
        if (advertisementOpt.isPresent()) {
            Advertisement advertisement = advertisementOpt.get();

            advertisement.setStatus(false);

            advertisement.setUpdatedAt(LocalDateTime.now());

            return advertisementRepository.save(advertisement);
        } else {
            throw new IllegalArgumentException("Anúncio não encontrado com o ID: " + dto.getId());
        }
    }

    @Transactional
    public Advertisement reactivateAdvertisement(AdvertisementDeactivateDTO dto) {
        Optional<Advertisement> advertisementOpt = advertisementRepository.findById(Long.valueOf(dto.getId()));
        if (advertisementOpt.isPresent()) {
            Advertisement advertisement = advertisementOpt.get();

            advertisement.setStatus(true);

            advertisement.setUpdatedAt(LocalDateTime.now());

            return advertisementRepository.save(advertisement);
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
