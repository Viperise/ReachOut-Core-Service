package com.reachout.ReachoutSystem.establishment.service;

import com.reachout.ReachoutSystem.establishment.dto.EstablishmentProductAddRequestDTO;
import com.reachout.ReachoutSystem.establishment.dto.ProductEditResponseDTO;
import com.reachout.ReachoutSystem.establishment.dto.ProductListResponseDTO;
import com.reachout.ReachoutSystem.establishment.entity.Establishment;
import com.reachout.ReachoutSystem.establishment.entity.Product;
import com.reachout.ReachoutSystem.establishment.repository.EstablishmentRepository;
import com.reachout.ReachoutSystem.establishment.repository.ProductRepository;
import com.reachout.ReachoutSystem.establishment.resources.ProductListConverter;
import com.reachout.ReachoutSystem.user.entity.Role;
import com.reachout.ReachoutSystem.user.entity.User;
import com.reachout.ReachoutSystem.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final UserRepository userRepository;
    private final EstablishmentRepository establishmentRepository;
    private final ProductRepository productRepository;

    public Page<ProductListResponseDTO> findAll(Pageable pageable, String establishmentId) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(ProductListConverter::productToProductListResponseConverter);
    }

    @Transactional
    public Optional<Product> findById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product;
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

    @Transactional
    public Product update(ProductEditResponseDTO productEditResponseDTO, String roleUidPermission) throws AccessDeniedException {
        Product existingProduct = productRepository.findById(productEditResponseDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + productEditResponseDTO.getId()));

        User user = userRepository.findByUid(roleUidPermission)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (user.getRole().equals(Role.SYSADMIN) || user.getRole().equals(Role.PARTNER_CLIENT) || user.getRole().equals(Role.CLIENT))
            throw new AccessDeniedException("Usuário não tem permissão para editar este produto.");

        existingProduct.setName(productEditResponseDTO.getName());
        existingProduct.setCategory(productEditResponseDTO.getCategory());
        existingProduct.setDescription(productEditResponseDTO.getDescription());
        existingProduct.setPrice(productEditResponseDTO.getPrice());
        existingProduct.setPhotoPath(productEditResponseDTO.getPhotoPath());

        return productRepository.save(existingProduct);
    }

    @Transactional
    public void delete(Long id, String roleUidPermission) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + id));

        User user = userRepository.findByUid(roleUidPermission)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (user.getRole().equals(Role.SYSADMIN) || user.getRole().equals(Role.PARTNER_CLIENT) || user.getRole().equals(Role.CLIENT))
            throw new IllegalStateException("Usuário não tem permissão para desativar este produto");

        existingProduct.setAvailable(false);
        existingProduct.setUpdatedAt(LocalDateTime.now());

        productRepository.save(existingProduct);
    }



}
