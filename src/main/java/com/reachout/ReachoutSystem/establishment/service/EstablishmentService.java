package com.reachout.ReachoutSystem.establishment.service;

import com.reachout.ReachoutSystem.establishment.dto.EstablishmentCreateRequestDTO;
import com.reachout.ReachoutSystem.establishment.dto.EstablishmentListResponseDTO;
import com.reachout.ReachoutSystem.establishment.dto.EstablishmentOwnerCreateRequestDTO;
import com.reachout.ReachoutSystem.establishment.dto.EstablishmentProductAddRequestDTO;
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

    public Optional<Establishment> findById(Long id) {
        return establishmentRepository.findById(id);
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
    public Establishment addProductsToEstablishment(Long establishmentId, List<EstablishmentProductAddRequestDTO> productDTOs, String roleUidPermission) {
        Optional<User> user = userRepository.findByUid(roleUidPermission);
        boolean hasValidRole = user
                .map(u -> u.getRole() == Role.SYSADMIN || u.getRole() == Role.PARTNER_CLIENT)
                .orElse(false);

        if (!hasValidRole)
            throw new EntityNotFoundException("Usuário não encontrado ou não tem permissão para realizar esta ação.");

        Establishment establishment = establishmentRepository.findById(establishmentId)
                .orElseThrow(() -> new EntityNotFoundException("Estabelecimento não encontrado..."));

        if (productDTOs != null && !productDTOs.isEmpty()) {
            List<Product> newProducts = productDTOs.stream().map(dto -> {
                Product product = new Product();
                product.setName(dto.getName());
                product.setDescription(dto.getDescription());
                product.setPrice(dto.getPrice());
                product.setPhotoPath(dto.getPhotoPath());
                product.setEstablishment(establishment);
                return product;
            }).toList();

            establishment.getProducts().addAll(newProducts);
            establishmentRepository.save(establishment);
        }

        return establishment;
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
