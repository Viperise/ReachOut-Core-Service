package com.reachout.ReachoutSystem.advertisement.repository;

import com.reachout.ReachoutSystem.advertisement.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
}
