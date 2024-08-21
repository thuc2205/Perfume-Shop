package com.example.thucbashop.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Builder
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    @NotBlank(message = "Hay Nhap So Dien Thoai")
    private String phoneNumber;

    private String address;

    @NotBlank(message = "khong duoc trong password")
    private String password;

    @NotBlank(message = "khong duoc trong retypePassword")
    @JsonProperty("retype_password")
    private String retypePassWord;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date birth;

    @JsonProperty("facebook_id")
    private int facebookAcountId;

    @JsonProperty("google_id")
    private int googleId;

    @NotNull(message = "Chon Vai Tro")
    @JsonProperty("role_id")
    private Long roleId;

}
