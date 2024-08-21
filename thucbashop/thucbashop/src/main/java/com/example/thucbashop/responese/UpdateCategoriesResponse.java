package com.example.thucbashop.responese;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCategoriesResponse {
    @JsonProperty("message")
    private String message;
    @JsonProperty("token")
    private String token;

}
