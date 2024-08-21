package com.example.thucbashop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1, message = "Order Id must be >0")
    private Long orderId;
    @JsonProperty("product_id")
    @Min(value = 1, message = "product Id must be >0")
    private Long productId;
    @Min(value = 1, message = "Price must be >0")
    private Float price;
    @JsonProperty("number_of_product")
    @Min(value = 1, message = "numberOfProduct must be >0")
    private int numberOfProduct;
    @JsonProperty("total_money")
    @Min(value = 1, message = "totalMoney must be >0")
    private Float totalMoney;

    private String color;

    @Override
    public String toString() {
        return "OrderDetailDTO{" +
                "orderId=" + orderId +
                ", productId=" + productId +
                ", price=" + price +
                ", numberOfProduct=" + numberOfProduct +
                ", totalMoney=" + totalMoney +
                ", color='" + color + '\'' +
                '}';
    }
}
