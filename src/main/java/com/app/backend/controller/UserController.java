package com.app.backend.controller;

import com.app.backend.dto.user.UserCreationDTO;
import com.app.backend.dto.user.UserLoginDTO;
import com.app.backend.dto.user.UserViewDTO;
import com.app.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping()
    public ResponseEntity<Object> getUser(@RequestBody UserLoginDTO userLoginDTO){
        if(userService.getUser(userLoginDTO) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.ok(userService.getUser(userLoginDTO));
    }
    @PostMapping(path = "/register")
    public ResponseEntity<?> register(@RequestBody UserCreationDTO userDto) {
        try {
            userDto.setPassword(passwordEncoder
                    .encode(userDto.getPassword()));
            return new ResponseEntity<>(userService.register(UserCreationDTO.dtoToModel(userDto)),
                    HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/activate")
    public ResponseEntity<?> activateUser(@RequestParam(value = "user") UUID userId) {
        try {
            return new ResponseEntity<>(userService.activateUser(userId), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO) {
        try {
            return new ResponseEntity<>(userService.login(userLoginDTO), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/check/{email}")
    public ResponseEntity<?> checkEmail(@PathVariable("email") String email){
        return ResponseEntity.ok(userService.checkEmail(email));
    }

}
