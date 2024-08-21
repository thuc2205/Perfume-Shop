package com.example.thucbashop.responese;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseResponse {
    @JsonProperty(  "created_at" )
    private LocalDateTime createdAt;
    @JsonProperty( "updated_at" )
    private LocalDateTime updatedAt;


}
