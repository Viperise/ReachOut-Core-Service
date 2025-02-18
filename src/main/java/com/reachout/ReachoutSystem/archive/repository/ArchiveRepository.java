package com.reachout.ReachoutSystem.archive.repository;

import com.reachout.ReachoutSystem.archive.entity.Archive;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchiveRepository extends JpaRepository<Archive, Long> {
}
