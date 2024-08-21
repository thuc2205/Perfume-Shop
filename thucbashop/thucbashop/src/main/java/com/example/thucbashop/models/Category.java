package com.example.thucbashop.models;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "categories")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name", nullable = false)//ko dc phép null
    private String name;
}
