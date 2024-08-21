package com.example.thucbashop.repositories;

import com.example.thucbashop.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepo extends JpaRepository<ProductImage,Long> {
    List<ProductImage> findByProductId(long id);
}
