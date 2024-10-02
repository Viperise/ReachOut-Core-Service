package com.reachout.ReachoutSystem.users.service;

import com.reachout.ReachoutSystem.users.dto.CreateNewUserRequestDTO;
import com.reachout.ReachoutSystem.users.dto.UserListResponseDTO;
import com.reachout.ReachoutSystem.users.entity.Role;
import com.reachout.ReachoutSystem.users.entity.User;
import com.reachout.ReachoutSystem.users.repository.UserRepository;
import com.reachout.ReachoutSystem.users.resources.UserListConverter;
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

    public Page<UserListResponseDTO> findAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(UserListConverter::userToUserListResponseConverter);
    }

    public Optional<User> findByUid(String uid) {
        return userRepository.findByUid(uid);
    }

    @Transactional
    public User save(CreateNewUserRequestDTO userDTO) throws Exception {
        if (userRepository.existsByEmail(userDTO.getEmail()))
            throw new DataIntegrityViolationException("Email já está em uso.");

        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setRole(Role.PARTNER_CLIENT);
        user.setStatus(true);
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        return email != null && pat.matcher(email).matches();
    }
}