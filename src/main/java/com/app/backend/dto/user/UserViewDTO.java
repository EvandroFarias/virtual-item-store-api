package com.app.backend.dto.user;

import com.app.backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserViewDTO {

    private UUID id;
    private String firstName;
    private String lastName;
    private String fullName;
    private Double balance;
    private Boolean isActive;
    private Date lastUpdateDate;


    public static UserViewDTO modelToDto(User user) {
        if (user == null) return null;

        return UserViewDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .balance(user.getBalance())
                .isActive(user.getIsActive())
                .lastUpdateDate(user.getLastUpdateDate())
                .build();
    }
}
