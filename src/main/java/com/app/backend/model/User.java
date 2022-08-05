package com.app.backend.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @Column(name = "id")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    private String firstName;
    private String lastName;
    private Double balance;
    @NotNull
    private String email;
    @NotNull
    private String password;
    private Date createdAt;
    private Date lastUpdateDate;
    private Boolean isActive;

    @PrePersist
    protected void onCreate() {
        this.setId(UUID.randomUUID());
        this.setBalance(0.0);
        this.setIsActive(false);
        this.setCreatedAt(new Date(System.currentTimeMillis()));
    }
}
