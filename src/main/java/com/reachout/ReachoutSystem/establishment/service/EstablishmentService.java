package com.reachout.ReachoutSystem.establishment.service;

import com.reachout.ReachoutSystem.establishment.dto.EstablishmentListResponseDTO;
import com.reachout.ReachoutSystem.establishment.entity.Establishment;
import com.reachout.ReachoutSystem.establishment.repository.EstablishmentRepository;
import com.reachout.ReachoutSystem.establishment.repository.ProductRepository;
import com.reachout.ReachoutSystem.establishment.resources.EstablishmentListConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstablishmentService {

    private final EstablishmentRepository establishmentRepository;
    private final ProductRepository productRepository;

    public Page<EstablishmentListResponseDTO> findAll(Pageable pageable) {
        Page<Establishment> establishments = establishmentRepository.findAll(pageable);
        return establishments.map(EstablishmentListConverter::establishmentToEstablishmentListResponseConverter);
    }
}
