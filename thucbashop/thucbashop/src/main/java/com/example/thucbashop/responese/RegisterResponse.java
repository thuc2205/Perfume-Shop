package com.example.thucbashop.responese;

import com.example.thucbashop.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {
    @JsonProperty("message")
    private String message;
    @JsonProperty("user")
    private User user;

}
