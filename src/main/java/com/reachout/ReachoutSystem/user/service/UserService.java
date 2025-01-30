package com.reachout.ReachoutSystem.user.service;

import com.reachout.ReachoutSystem.user.dto.UserCreateRequestDTO;
import com.reachout.ReachoutSystem.user.dto.UserDetailResponseDTO;
import com.reachout.ReachoutSystem.user.dto.UserListResponseDTO;
import com.reachout.ReachoutSystem.user.entity.Document;
import com.reachout.ReachoutSystem.user.entity.Role;
import com.reachout.ReachoutSystem.user.entity.User;
import com.reachout.ReachoutSystem.user.repository.DocumentRepository;
import com.reachout.ReachoutSystem.user.repository.UserRepository;
import com.reachout.ReachoutSystem.user.resources.UserListConverter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;

    // LISTA TODOS OS USUÁRIOS (ATIVOS E INATIVOS)
    public Page<UserListResponseDTO> findAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(UserListConverter::userToUserListResponseConverter);
    }

    // FILTRO DE CLIENTES PARCEIROS ATIVOS
    public Page<UserListResponseDTO> findClientPartnersActive(String uid, Pageable pageable) {
        Page<User> activePartners = userRepository.findByStatusAndRoleAndUid(true, Role.PARTNER_CLIENT, uid, pageable);

        return  activePartners.map(UserListConverter::userToUserListResponseConverter);
    }

    // ENCONTRA USUÁRIO POR UID
    public Optional<UserDetailResponseDTO> findByUid(String uid) {
        return userRepository.findByUid(uid)
                .map(user -> new UserDetailResponseDTO(
                        user.getUid(),
                        user.getName(),
                        user.getRole().toString(),
                        user.getEmail(),
                        user.getBirthday(),
                        user.getDocument().getDocumentType(),
                        user.getDocument().getDocumentNumber(),
                        user.getAddress(),
                        user.getPhone(),
                        user.getPhotoPath()
                ));
    }

    // SALVA UM NOVO USUÁRIO
    @Transactional
    public User save(UserCreateRequestDTO userDTO, String roleUidPermission, Role role) throws Exception {
        if (userRepository.existsByEmail(userDTO.getEmail()))
            throw new DataIntegrityViolationException("Email já está em uso.");

        Document document = new Document();
        document.setDocumentNumber(userDTO.getDocumentNumber());
        document.setDocumentType(userDTO.getDocumentType());
        document.setCreatedAt(LocalDateTime.now());
        document.setUpdatedAt(LocalDateTime.now());
        documentRepository.save(document);

        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setBirthday(userDTO.getBirthday());
        user.setRole(role);
        user.setStatus(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setPhotoPath(userDTO.getPhotoPath());
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());
        user.setUid(userDTO.getUid());
        user.setDocument(document);

        return userRepository.save(user);
    }

    // ATUALIZA UM USUÁRIO
    @Transactional
    public User update(UserCreateRequestDTO userDTO) throws Exception {
        User user = userRepository.findByUid(userDTO.getUid())
                        .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setBirthday(userDTO.getBirthday());
        user.setAddress(userDTO.getAddress());
        user.setPhotoPath(userDTO.getPhotoPath());

        if (userDTO.getDocumentNumber() != null) {
            if (user.getDocument() == null)
                user.setDocument(new Document());
            user.getDocument().setDocumentNumber(userDTO.getDocumentNumber());
            user.getDocument().setDocumentType(userDTO.getDocumentType());
        }

        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    // DESATIVA UM USUÁRIO
    @Transactional
    public void disable(String uid) {
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (user.getRole() == Role.SYSADMIN || user.getRole() == Role.CLIENT)
            throw new IllegalStateException("Não é permitido desativar usuários com o papel de SYSADMIN ou ADMIN.");

        user.setStatus(false);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
    }
}