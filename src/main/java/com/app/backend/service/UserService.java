package com.app.backend.service;

import com.app.backend.dto.user.UserLoginDTO;
import com.app.backend.dto.user.UserViewDTO;
import com.app.backend.model.User;
import com.app.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public static boolean patternMatches(String email) {
        String regexPattern = "^(.+)@(\\S+)$";

        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }

    public UserViewDTO getUser(UserLoginDTO user){
        return UserViewDTO.modelToDto(userRepository.findByEmail(user.getEmail()).orElse(null));
    }

    public UserViewDTO register(User user) throws Exception {
        if (user.getPassword().length() <= 0
                || user.getEmail().length() <= 0
                || user.getFirstName().length() <= 0
                || user.getLastName().length() <= 0) {
            throw new IllegalArgumentException("Missing fields !");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new Exception("E-mail already registered");
        }
        if (!patternMatches(user.getEmail())) {
            throw new Exception("E-mail not valid");
        }
        userRepository.save(user);
        return UserViewDTO.modelToDto(user);
//       return "Activate your account on: http://localhost:8080/api/v1/user/activate?user="+user.getId();
    }

    public UserViewDTO activateUser(UUID userId) throws Exception {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new Exception("User does not Exists");
        }

        if (user.getIsActive()) {
            throw new Exception("User already activated");
        }

        user.setIsActive(true);
        user.setLastUpdateDate(new Date(System.currentTimeMillis()));
        userRepository.save(user);
        return UserViewDTO.modelToDto(user);

    }

    public String login(UserLoginDTO userLoginDTO) throws Exception {
        User user = userRepository.findByEmail(userLoginDTO.getEmail()).orElse(null);
        if (user == null) throw new Exception("User name or password doesn't match");

        if(!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            throw new Exception("User name or password doesn't match");
        }
        return TokenService.returnToken(userLoginDTO);
    }

    public boolean checkEmail(String email){
        return userRepository.existsByEmail(email);
    }
}
