package com.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
}
