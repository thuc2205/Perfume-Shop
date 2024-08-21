package com.example.thucbashop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {


    @JsonProperty("user_id")
    @Min(value = 1, message = "User's Id must be >0")
    private Long userId;
    @JsonProperty("fullname")
    private String fullName;

    private String email;

    @JsonProperty("phone_number")
    @Size(min = 10,message = " sdt phải 10 so")
    @NotBlank(message = "phone isEmpy")
    private String phoneNumber;
    private String address;
    private String note;
    @JsonProperty("total_money")
    @Min(value = 0,message = "Tong Tien lon hon > 0")
    private Float totalMoney;
    @JsonProperty("shipping_method")
    private String shippingMethod;
    @JsonProperty("shipping_address")
    private String shippingAddress;
    @JsonProperty("payment_method")
    private String  paymentMethod;

    @JsonProperty("shipping_date")
    private LocalDate shippingDate;
    @JsonProperty("cart_items")
    private List<CartItemDTO> cartItem;


}
