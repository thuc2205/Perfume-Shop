package com.example.thucbashop.repositories;

import com.example.thucbashop.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}
    // tìm đơn hàng user nào đó
