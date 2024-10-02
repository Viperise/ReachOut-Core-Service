package com.reachout.ReachoutSystem.users.repository;


import com.reachout.ReachoutSystem.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByUid(String uid);

    User findByName(String name);
}
