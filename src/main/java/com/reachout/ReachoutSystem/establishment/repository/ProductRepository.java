package com.reachout.ReachoutSystem.establishment.repository;

import com.reachout.ReachoutSystem.establishment.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
