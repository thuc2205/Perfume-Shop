package com.example.thucbashop.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Product extends BaseModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name" ,nullable = false,length = 350)
    private String name;
    @Column(name = "price")
    private Float price;
    @Column(name = "thumbnail" ,length = 300)
    private String thumbnail;
    @Column(name = "description" )
    private String description;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;
    @JsonIgnore
    @JsonProperty("product_images")
    @OneToMany(mappedBy = "product")
    private List<ProductImage> productImages;



}
