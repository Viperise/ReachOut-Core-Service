package com.reachout.ReachoutSystem.users.repository;

import com.reachout.ReachoutSystem.users.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

}
