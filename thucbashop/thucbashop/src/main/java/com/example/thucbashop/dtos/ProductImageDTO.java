package com.example.thucbashop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductImageDTO {

    @JsonProperty("product_id")
    @Min(value = 1,message = "PRODUCT ID must be >0")
    private Long productId;

    @Size(min = 5,max = 200,message = "Image's name")
    @JsonProperty("product_url")
    private String imageUrl;
}
