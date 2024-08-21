package com.example.thucbashop.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Data
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseModel {

    @Column(name = "create_at" )
    private LocalDateTime createdAt;
    @Column(name = "update_at" )
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCrete(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt = LocalDateTime.now();
    }

}
