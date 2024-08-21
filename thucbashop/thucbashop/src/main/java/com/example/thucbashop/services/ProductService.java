package com.example.thucbashop.services;

import com.example.thucbashop.dtos.ProductDTO;
import com.example.thucbashop.dtos.ProductImageDTO;
import com.example.thucbashop.exceptions.DataNotFound;
import com.example.thucbashop.exceptions.InvalidParamException;
import com.example.thucbashop.models.Category;
import com.example.thucbashop.models.Product;
import com.example.thucbashop.models.ProductImage;
import com.example.thucbashop.repositories.CategoryRepo;
import com.example.thucbashop.repositories.ProductImageRepo;
import com.example.thucbashop.repositories.ProductRepo;
import com.example.thucbashop.responese.ProductResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final ProductImageRepo productImageRepo;
    @Override
    @Transactional
    public Product createProduct(ProductDTO productDTO) throws DataNotFound {
      Category existingCategory= categoryRepo.findById
                      (productDTO.getCategoryId())
               .orElseThrow(()->new DataNotFound("KHONG THAY CATEGORY VOI ID : "+productDTO.getCategoryId()));

       Product newProduct = Product.builder()
               .name(productDTO.getName())
               .price(productDTO.getPrice())
               .thumbnail(productDTO.getThumbnail())
               .category(existingCategory)
               .thumbnail("")
               .description(productDTO.getDescription())
               .build();
        return productRepo.save(newProduct);
}

    @Override
    public Product getProductById(long id) throws Exception {
        return productRepo.findById(id).orElseThrow(()-> new DataNotFound("KHONG TIM THAY ID : "+id));
    }

    @Override
    public Page<ProductResponse> getAllProduct(String keyword, Long categoryId ,PageRequest request) {
        //lấy danh sách sản phẩm page và limit
        if (categoryId != null && categoryId != 0) {
            return productRepo.searchProducts(categoryId, keyword, request).map(ProductResponse::fromProduct);
        } else {
            // Nếu không, sử dụng phương thức findAll để lấy tất cả sản phẩm
            return productRepo.findAll(request).map(ProductResponse::fromProduct);
        }
    }

    @Override
    @Transactional
    public Product updateProduct(long id, ProductDTO productDTO) throws Exception {
        Product existingProduct = getProductById(id);
        if(existingProduct !=null){
            //copy ProductDTAO _> product
            //có thể dùng ModelMapper
            Category existingCategory= categoryRepo.findById
                            (productDTO.getCategoryId())
                    .orElseThrow(
                            ()->new DataNotFound("KHONG THAY CATEGORY VOI ID : "+productDTO.getCategoryId())
                    );
            existingProduct.setName(productDTO.getName());
            existingProduct.setCategory(existingCategory);
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setThumbnail(productDTO.getThumbnail());
            return productRepo.save(existingProduct);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteProduct(long id) {
        Optional<Product> optionalProduct = productRepo.findById(id);
        optionalProduct.ifPresent(productRepo::delete);

    }

    @Override
    public boolean existsByName(String name) {
        return productRepo.existsByName(name);
    }
    @Override
    public ProductImage createProductImage(long productId, ProductImageDTO productImageDTO) throws Exception{
        Product existingProduct= productRepo.findById
                        (productId)
                .orElseThrow(
                        ()->new DataNotFound("KHONG THAY PRODUCT VOI ID : "+productImageDTO.getProductId()));
        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .url(productImageDTO.getImageUrl())
                .build();
        //không cho insert qua 5 anh cho1 sp
        int size = productImageRepo.findByProductId(productId).size();
        if(size>= ProductImage.MAXIMUM_IMAGE_PER_PRODUCT){
            throw new InvalidParamException("Anh Khong Duoc >5 ẢNH ");
        }
        return productImageRepo.save(newProductImage);
    }

    @Override
    public List<Product> findProductsByIds(List<Long> productIds) {
        return productRepo.findAllByIds(productIds);
    }


}
