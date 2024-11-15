package com.reachout.ReachoutSystem.analytics.service;

import com.reachout.ReachoutSystem.analytics.dto.UserRegisteredListDTO;
import com.reachout.ReachoutSystem.user.entity.User;
import com.reachout.ReachoutSystem.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAnalyticsService {
    private final UserRepository userRepository;

    @Transactional
    public Page<UserRegisteredListDTO> getAllUserRegistereds (Pageable page) {
        Page<User> users = userRepository.findAll(page);

        return users.map(user -> new UserRegisteredListDTO(
                user.getUid(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getRole().toString()
        ));
    }
}
