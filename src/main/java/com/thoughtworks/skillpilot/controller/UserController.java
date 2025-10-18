// UserController.java
package com.thoughtworks.skillpilot.controller;


import com.thoughtworks.skillpilot.dto.UserDTO;
import com.thoughtworks.skillpilot.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/register-user")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody UserDTO userDTO) {
        userService.registerNewUser(userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail(),userDTO.getRole());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }


}