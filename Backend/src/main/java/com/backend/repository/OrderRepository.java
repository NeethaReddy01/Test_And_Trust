package com.backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.modal.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.orderStatus IN (0,1, 2, 3, 4)")
	public List<Order> getUsersOrders(@Param("userId") Long userId);
	
	List<Order> findAllByOrderByCreatedAtDesc();
	
	 List<Order> findByUserId(Long userId);
	    List<Order> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);
}