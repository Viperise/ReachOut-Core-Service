package com.reachout.ReachoutSystem.establishment.repository;

import com.reachout.ReachoutSystem.establishment.entity.Establishment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstablishmentRepository extends JpaRepository<Establishment, Long> {
    Establishment findByName(String name);
}
