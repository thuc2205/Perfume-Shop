package com.example.thucbashop.controllers;

import com.example.thucbashop.dtos.CategoryDTO;
import com.example.thucbashop.models.Category;
import com.example.thucbashop.responese.UpdateCategoriesResponse;
import com.example.thucbashop.services.CategoryService;
import com.example.thucbashop.components.LocalizationUtils;
import com.example.thucbashop.components.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
//@Validated
@RequiredArgsConstructor
public class  CategoryController {

    private final CategoryService categoryService;
    private final LocalizationUtils localizationUtils;


    @PostMapping("")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult result){
        if(result.hasErrors()){
            List<String> errorMessage =  result.getFieldErrors().
                    stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(localizationUtils.getLocalizationUtils(MessageKeys.CREATE_CATEGORY_FAILED));
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(localizationUtils.getLocalizationUtils(MessageKeys.CREATE_CATEGORY_SUCCESSFULLY));
    }

    @GetMapping("")
    public ResponseEntity<List<Category>> getAllCategories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ){
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateCategoriesResponse> updateCategory(@PathVariable long id ,
                                                                   @RequestBody CategoryDTO categoryDTO){
        categoryService.updateCategory(id,categoryDTO);

        return ResponseEntity.ok(UpdateCategoriesResponse.builder()
                .message(localizationUtils.getLocalizationUtils(MessageKeys.MessageKeys_UPDATE_CATEGORY_SUCCESSFULLY))
                .build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(localizationUtils.getLocalizationUtils(MessageKeys.DELETE_CATEGORY_SUCCESSFULLY));
    }
}
