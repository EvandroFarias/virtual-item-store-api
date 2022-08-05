package com.app.backend.model;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private User user;
    private String name;
    private Double price;
    private Double discount;
    private Boolean isActive;
    private Date createdAt;
    private Date lastUpdateDate;

    @PrePersist
    protected void onCreate(){
        this.setId(UUID.randomUUID());
        this.setIsActive(true);
        this.setCreatedAt(new Date(System.currentTimeMillis()));
    }
}
