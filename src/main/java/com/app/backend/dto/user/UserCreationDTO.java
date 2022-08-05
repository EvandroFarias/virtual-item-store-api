package com.app.backend.dto.user;

import com.app.backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public static User dtoToModel(UserCreationDTO dto){
        if(dto == null) return null;

        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }
}
