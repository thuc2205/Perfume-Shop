package com.example.thucbashop.responese;

import com.example.thucbashop.models.Product;
import com.example.thucbashop.models.ProductImage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse extends BaseResponse {
    // đọc ữ lệu trả về
    private  Long id;
    private String name;

    private float price;
    private String thumbnail;
    private String description;
    @JsonProperty("category_id")
    private Long categoryId;
    @JsonProperty("product_images")
    private List<ProductImage> productImages = new ArrayList<>();

    public static ProductResponse fromProduct(Product product){
            ProductResponse productResponse= ProductResponse.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .thumbnail(product.getThumbnail())
                    .description(product.getDescription())
                    .categoryId(product.getCategory().getId())
                    .productImages(product.getProductImages())
                    .build();
            productResponse.setCreatedAt(product.getCreatedAt());
            productResponse.setUpdatedAt(product.getUpdatedAt());
            return productResponse;


    }


}
