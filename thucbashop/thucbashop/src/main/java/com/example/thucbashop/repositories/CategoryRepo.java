package com.example.thucbashop.repositories;

import com.example.thucbashop.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category,Long> {
}
