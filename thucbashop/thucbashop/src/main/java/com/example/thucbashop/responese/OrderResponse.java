package com.example.thucbashop.responese;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse extends BaseResponse{
    private long id;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String address;
    private String note;

    @JsonProperty("order_date")
    private LocalDateTime orderDate;

    private String status;

    @JsonProperty("total_money")
    private Float totalMoney;

    @JsonProperty(  "shipping_method")
    private String shippingMethod;

    @JsonProperty( "shipping_date")
    private String shippingDate;

    @JsonProperty( "tracking_number")
    private String trackingNumber;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty( "active")
    private Boolean active;//admin
}
