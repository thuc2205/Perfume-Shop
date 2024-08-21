package com.example.thucbashop.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "product_image")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class ProductImage {
    public static final  int MAXIMUM_IMAGE_PER_PRODUCT=5;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id" )
    private Product product;

    @Column(name = "image_url",length = 300)
    private String url;


}
