package com.example.thucbashop.responese;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProductImageRespon {
    private Long id;
    @JsonProperty("image_url")
    private String imageUrl;
}
