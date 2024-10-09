package com.reachout.ReachoutSystem.users.service;

import com.reachout.ReachoutSystem.users.dto.UserCreateRequestDTO;
import com.reachout.ReachoutSystem.users.dto.UserListResponseDTO;
import com.reachout.ReachoutSystem.users.entity.Document;
import com.reachout.ReachoutSystem.users.entity.Role;
import com.reachout.ReachoutSystem.users.entity.User;
import com.reachout.ReachoutSystem.users.repository.DocumentRepository;
import com.reachout.ReachoutSystem.users.repository.UserRepository;
import com.reachout.ReachoutSystem.users.resources.UserListConverter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;

    public Page<UserListResponseDTO> findAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(UserListConverter::userToUserListResponseConverter);
    }

    public Optional<User> findByUid(String uid) {
        return userRepository.findByUid(uid);
    }

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


    @Transactional
    public void delete(String uid) {
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (user.getRole() == Role.SYSADMIN || user.getRole() == Role.ADMIN)
            throw new IllegalStateException("Não é permitido desativar usuários com o papel de SYSADMIN ou ADMIN.");

        user.setStatus(false);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
    }
}