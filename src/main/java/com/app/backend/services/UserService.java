package com.app.backend.services;

import com.app.backend.customException.AlreadyRegisteredException;
import com.app.backend.customException.RedundancyExcpetion;
import com.app.backend.dtos.user.UserLoginDTO;
import com.app.backend.dtos.user.UserViewDTO;
import com.app.backend.models.User;
import com.app.backend.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@Slf4j
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

    public UserViewDTO getUser(UserLoginDTO user) {
        try {
            return UserViewDTO.modelToDto(userRepository.findByEmail(user.getEmail()).orElse(null));
        } catch (Exception e) {
            log.error("Error finding user at User Service");
            e.printStackTrace();
            throw e;
        }
    }

    public UserViewDTO register(User user) throws Exception {
        try {
            this.isValidCreation(user);

            userRepository.save(user);
            return UserViewDTO.modelToDto(user);
//          return "Activate your account on: http://localhost:8080/api/v1/user/activate?user="+user.getId();

        } catch (Exception e) {
            log.error("Error creating user at User Service");
            e.printStackTrace();
            throw e;
        }
    }

    public UserViewDTO activateUser(UUID userId) throws Exception {
        try {
            User user = userRepository.findById(userId).orElse(null);

            this.isValidActivation(user);

            user.setIsActive(true);
            user.setLastUpdateDate(new Date(System.currentTimeMillis()));
            userRepository.save(user);
            return UserViewDTO.modelToDto(user);
        } catch (Exception e) {
            log.error("Error activating user at User Service");
            e.printStackTrace();
            throw e;
        }
    }

    public String login(UserLoginDTO userLoginDTO) throws Exception {
        try {
            User user = userRepository.findByEmail(userLoginDTO.getEmail()).orElse(null);

            if (user == null) throw new IllegalArgumentException("User name or password doesn't match");
            if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
                throw new IllegalArgumentException("User name or password doesn't match");
            }

            return TokenService.returnToken(userLoginDTO);
        } catch (Exception e){
            log.error("Error accepting login user at User Service");
            e.printStackTrace();
            throw e;
        }

    }

    public boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void isValidCreation(User user) throws Exception {
        if (user.getPassword().length() <= 0
                || user.getEmail().length() <= 0
                || user.getFirstName().length() <= 0
                || user.getLastName().length() <= 0) {
            throw new IllegalArgumentException("Missing fields !");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new AlreadyRegisteredException("E-mail already registered");
        }
        if (!patternMatches(user.getEmail())) {
            throw new IllegalArgumentException("E-mail not valid");
        }
    }

    public void isValidActivation(User user) throws Exception {
        if (user == null) {
            throw new NoSuchElementException("User does not Exists");
        }

        if (user.getIsActive()) {
            throw new RedundancyExcpetion("User already activated");
        }
    }
}
