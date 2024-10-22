package com.reachout.ReachoutSystem.establishment.service;

import com.reachout.ReachoutSystem.establishment.dto.*;
import com.reachout.ReachoutSystem.establishment.entity.Establishment;
import com.reachout.ReachoutSystem.establishment.entity.Product;
import com.reachout.ReachoutSystem.establishment.repository.EstablishmentRepository;
import com.reachout.ReachoutSystem.establishment.repository.ProductRepository;
import com.reachout.ReachoutSystem.establishment.resources.EstablishmentListConverter;
import com.reachout.ReachoutSystem.users.entity.Document;
import com.reachout.ReachoutSystem.users.entity.Role;
import com.reachout.ReachoutSystem.users.entity.User;
import com.reachout.ReachoutSystem.users.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EstablishmentService {

    private final UserRepository userRepository;
    private final EstablishmentRepository establishmentRepository;
    private final ProductRepository productRepository;

    public Page<EstablishmentListResponseDTO> findAll(Pageable pageable) {
        Page<Establishment> establishments = establishmentRepository.findAll(pageable);
        return establishments.map(EstablishmentListConverter::establishmentToEstablishmentListResponseConverter);
    }

    @Transactional
    public Optional<Establishment> findById(Long id) {
        Optional<Establishment> establishment = establishmentRepository.findById(id);
        establishment.ifPresent(e -> Hibernate.initialize(e.getProducts()));
        return establishment;
    }

    @Transactional
    public Establishment save(EstablishmentCreateRequestDTO establishmentDTO, String roleUidPermission) {
        Optional<User> userOptional = userRepository.findByUid(roleUidPermission);

        if (userOptional.isEmpty())
            throw new EntityNotFoundException("Usuário não encontrado.");

        User user = userOptional.get();

        if (establishmentDTO.getOwner().getName().isEmpty())
            throw new EntityNotFoundException("Não é possível adicionar um novo Estabelecimento sem um Cliente Parceiro como Dono deste Estabelecimento");

        if (establishmentDTO.getName() == null || establishmentDTO.getName().isEmpty())
            throw new IllegalArgumentException("O nome do estabelecimento é obrigatório.");

        if (user.getRole().equals(Role.PARTNER_EMPLOYEE))
            throw new EntityNotFoundException("O Usuário não tem permissão para realizar esta ação.");

        User owner = userRepository.findByUid(establishmentDTO.getOwner().getUid())
                .orElseThrow(() -> new EntityNotFoundException("Usuário associado à 'Dono de Estabelecimento' não encontrado."));

        Establishment establishment = getEstablishment(establishmentDTO);
        establishment.setOwner(owner);

        return establishmentRepository.save(establishment);
    }

    @Transactional
    public Establishment update(Long establishmentId, EstablishmentEditRequestDTO establishmentDTO, String roleUidPermission) {
        System.out.println("Atualizando estabelecimento com ID: " + establishmentId);
        Optional<Establishment> establishmentOptional = establishmentRepository.findById(establishmentId);
        Optional<User> userOptional = userRepository.findByUid(roleUidPermission);

        if (userOptional.isEmpty())
            throw new EntityNotFoundException("Usuário não encontrado.");


        if (establishmentOptional.isEmpty())
            throw new EntityNotFoundException("Estabelecimento não existe.");

        User user = userOptional.get();
        Establishment establishment = establishmentOptional.get();

        if (user.getRole().equals(Role.PARTNER_EMPLOYEE))
            throw new EntityNotFoundException("O Usuário não tem permissão para realizar esta ação.");

        establishment.setName(establishmentDTO.getName());
        establishment.setType(establishmentDTO.getType());
        establishment.setDescription(establishmentDTO.getDescription());
        establishment.setPhotoPath(establishmentDTO.getPhotoPath());
        establishment.setAddress(establishmentDTO.getAddress());
        establishment.setPhone(establishmentDTO.getPhone());

        return establishmentRepository.save(establishment);
    }

    @Transactional
    public void delete(Long id, String roleUidPermission) {
        Establishment establishment = establishmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estabelecimento não encontrado"));

        User user = userRepository.findByUid(roleUidPermission)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));


        if (user.getRole() == Role.PARTNER_EMPLOYEE)
            throw new IllegalStateException("Não é permitido desativar Estabelecimentos com o papel de CLIENT_EMPLOYEE.");

        establishment.setStatus(false);
        establishment.setUpdatedAt(LocalDateTime.now());

        establishmentRepository.save(establishment);
    }
    

    private static Establishment getEstablishment(EstablishmentCreateRequestDTO establishmentDTO) {
        Establishment establishment = new Establishment();

        establishment.setName(establishmentDTO.getName());
        establishment.setType(establishmentDTO.getType());
        establishment.setDescription(establishmentDTO.getDescription());
        establishment.setPhotoPath(establishmentDTO.getPhotoPath());
        establishment.setAddress(establishmentDTO.getAddress());
        establishment.setPhone(establishmentDTO.getPhone());

        EstablishmentOwnerCreateRequestDTO ownerDTO = establishmentDTO.getOwner();
        if (ownerDTO != null) {
            User owner = new User();
            owner.setUid(ownerDTO.getUid());
            owner.setName(ownerDTO.getName());
            owner.setEmail(ownerDTO.getEmail());
            owner.setPhone(ownerDTO.getPhone());

            if (ownerDTO.getDocument() != null) {
                Document document = new Document();
                document.setDocumentType(ownerDTO.getDocument().getDocumentType());
                document.setDocumentNumber(ownerDTO.getDocument().getDocumentNumber());

                owner.setDocument(document);
            }

            establishment.setOwner(owner);
        }

        List<EstablishmentProductAddRequestDTO> productDTOs = establishmentDTO.getProducts();
        if (productDTOs != null && !productDTOs.isEmpty()) {
            List<Product> products = productDTOs.stream().map(dto -> {
                Product product = new Product();
                product.setName(dto.getName());
                product.setAvailable(true);
                product.setCategory(dto.getCategory());
                product.setDescription(dto.getDescription());
                product.setPrice(dto.getPrice());
                product.setPhotoPath(dto.getPhotoPath());
                product.setEstablishment(establishment);
                return product;
            }).toList();

            establishment.setProducts(products);
        }

        establishment.setStatus(true);
        establishment.setCreatedAt(LocalDateTime.now());
        establishment.setUpdatedAt(LocalDateTime.now());

        return establishment;
    }
}
