package com.reachout.ReachoutSystem.user.repository;


import com.reachout.ReachoutSystem.user.entity.Role;
import com.reachout.ReachoutSystem.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByUid(String uid);

    User findByName(String name);

    Page<User> findByStatusAndRoleAndUid(boolean status, Role role, String uid, Pageable pageable);
}
