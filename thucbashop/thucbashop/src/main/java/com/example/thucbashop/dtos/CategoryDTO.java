package com.example.thucbashop.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    @NotEmpty(message = "Category name không để trông")
    private String name;

}
