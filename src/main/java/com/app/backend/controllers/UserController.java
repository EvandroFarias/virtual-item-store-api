package com.app.backend.controllers;

import com.app.backend.customException.AlreadyRegisteredException;
import com.app.backend.customException.RedundancyExcpetion;
import com.app.backend.dtos.user.UserCreationDTO;
import com.app.backend.dtos.user.UserLoginDTO;
import com.app.backend.services.UserService;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiOperation;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping()
    @ApiOperation(value = "Returns user information.")
    public ResponseEntity<Object> getUser(@RequestBody UserLoginDTO userLoginDTO){
        if(userService.getUser(userLoginDTO) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.ok(userService.getUser(userLoginDTO));
    }
    @PostMapping(path = "/register")
    @ApiOperation(value = "Creates an user.")
    public ResponseEntity<?> register(@RequestBody UserCreationDTO userDto) {
        try {
            userDto.setPassword(passwordEncoder
                    .encode(userDto.getPassword()));
            return ResponseEntity.status(201)
                    .body(userService.register(UserCreationDTO.dtoToModel(userDto)));
        } catch (AlreadyRegisteredException | IllegalArgumentException ex) {
            return ResponseEntity.status(400).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @GetMapping(path = "/activate")
    @ApiOperation(value = "Activate an user.")
    public ResponseEntity<?> activateUser(@RequestParam(value = "user") UUID userId) {
        try {
            return ResponseEntity.status(200)
                    .body(userService.activateUser(userId));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        }  catch (RedundancyExcpetion ex) {
            return ResponseEntity.status(400).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @PostMapping(path = "/login")
    @ApiOperation(value = "Returns a JWT if login succeeded.")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO) {
        try {
            return ResponseEntity.status(200).body(userService.login(userLoginDTO));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(401).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @GetMapping(path = "/check/{email}")
    @ApiOperation(value = "Verify if the email passed is already in use.")
    public ResponseEntity<?> checkEmail(@PathVariable("email") String email){
        return ResponseEntity.ok(userService.checkEmail(email));
    }

    @PostMapping(path = "/balance",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Adds balance to the user")
    public ResponseEntity<?> addBalance(@RequestParam(value = "user") UUID userId,
                                        @RequestBody Double amount){
        try {
            return ResponseEntity.ok(userService.addBalance(userId, amount));
        } catch (NoSuchElementException ex){
            return  ResponseEntity.status(404).body(ex.getMessage());
        } catch (Exception ex){
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }
}
