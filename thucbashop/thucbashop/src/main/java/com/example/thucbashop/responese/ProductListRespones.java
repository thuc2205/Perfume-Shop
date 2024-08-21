package com.example.thucbashop.responese;

import com.example.thucbashop.models.Product;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class ProductListRespones {
    private List<ProductResponse> products;
    private int totalPage;
}
