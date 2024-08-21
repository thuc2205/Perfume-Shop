    package com.example.thucbashop.dtos;

    import com.example.thucbashop.models.ProductImage;
    import com.fasterxml.jackson.annotation.JsonProperty;
    import jakarta.validation.constraints.Min;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.Size;
    import lombok.*;
    import org.springframework.web.multipart.MultipartFile;

    import java.util.ArrayList;
    import java.util.List;
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public class ProductDTO {
        @NotBlank(message = "title is required")
        @Size(min = 3,max = 200 ,message = "Không đủ kí tự 3 -200")
        private String name;

        @Min(value = 0,message = "Nhỏ ")
        private float price;
        private String thumbnail;
        private String description;
        @JsonProperty("category_id")
        private Long categoryId;







    }
