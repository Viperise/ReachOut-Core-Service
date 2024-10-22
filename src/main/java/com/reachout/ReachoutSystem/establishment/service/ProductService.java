package com.reachout.ReachoutSystem.establishment.service;

import com.reachout.ReachoutSystem.establishment.dto.EstablishmentProductAddRequestDTO;
import com.reachout.ReachoutSystem.establishment.entity.Establishment;
import com.reachout.ReachoutSystem.establishment.entity.Product;
import com.reachout.ReachoutSystem.establishment.repository.EstablishmentRepository;
import com.reachout.ReachoutSystem.establishment.repository.ProductRepository;
import com.reachout.ReachoutSystem.users.entity.Role;
import com.reachout.ReachoutSystem.users.entity.User;
import com.reachout.ReachoutSystem.users.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final UserRepository userRepository;
    private final EstablishmentRepository establishmentRepository;
    private final ProductRepository productRepository;



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
}
