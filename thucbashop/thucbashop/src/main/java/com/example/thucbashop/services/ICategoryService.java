package com.example.thucbashop.services;

import com.example.thucbashop.dtos.CategoryDTO;
import com.example.thucbashop.models.Category;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ICategoryService {
    Category createCategory(CategoryDTO category);

    Category getCategoryById(long id);

    List<Category> getAllCategories();
    Category updateCategory(long id,CategoryDTO category);
    void deleteCategory(long id);

}
