package com.example.thucbashop.services;

import com.example.thucbashop.dtos.CategoryDTO;
import com.example.thucbashop.models.Category;
import com.example.thucbashop.repositories.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    private final CategoryRepo categoryRepo;
    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        Category newCategory = Category.builder()
                .name(categoryDTO.getName())
                .build();
        return categoryRepo.save(newCategory);
    }

    @Override
    public Category getCategoryById(long id) {
        return categoryRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Ko TIm Thay Danh Muc")); //orElse(null);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
    public Category updateCategory(long id,@RequestBody CategoryDTO categoryDTO) {
        Category existingCategory =  getCategoryById(id);
        existingCategory.setName(categoryDTO.getName());
        categoryRepo.save(existingCategory);
        return existingCategory;
    }

    @Override // xóa cứng
    public void deleteCategory(long id) {
         categoryRepo.deleteById(id);
    }
}
