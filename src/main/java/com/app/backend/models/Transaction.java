package com.app.backend.models;

import com.app.backend.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {

    @Id
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private User buyer;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private User seller;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Product product;
    private Double price;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    private Date createdAt;
    private Date lastUpdateDate;

    @PrePersist
    public void onCreate(){
        this.setId(UUID.randomUUID());
        this.setCreatedAt(new Date(System.currentTimeMillis()));
    }

}
