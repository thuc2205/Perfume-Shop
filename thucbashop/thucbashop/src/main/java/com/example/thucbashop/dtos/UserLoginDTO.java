package com.example.thucbashop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginDTO {
    @JsonProperty("phone_number")
    @NotBlank(message = "Hay Nhap So Dien Thoai")
    private String phoneNumber;

    @NotBlank(message = "khong duoc trong password")
    private String password;
    @JsonProperty("role_id")
    private Long roleId;

































}
