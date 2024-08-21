package com.example.thucbashop.services;

import com.example.thucbashop.dtos.ProductDTO;
import com.example.thucbashop.dtos.ProductImageDTO;
import com.example.thucbashop.models.Product;
import com.example.thucbashop.models.ProductImage;
import com.example.thucbashop.responese.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;


public interface IProductService {
    public Product createProduct(ProductDTO productDTO) throws Exception;
    Product getProductById(long id) throws Exception;
    Page<ProductResponse>getAllProduct(String keyword, Long categoryId ,PageRequest request);
    Product updateProduct(long id, ProductDTO productDTO) throws Exception;
    void deleteProduct(long id);
    boolean existsByName(String name);
    ProductImage createProductImage(long productId, ProductImageDTO productImageDTO) throws Exception;

    public List<Product> findProductsByIds(List<Long>productIds);


}
