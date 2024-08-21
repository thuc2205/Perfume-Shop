package com.example.thucbashop.repositories;

import com.example.thucbashop.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepo extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrderId(Long orderId);
}
