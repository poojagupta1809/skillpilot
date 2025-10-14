// UserController.java
package com.thoughtworks.skillpilot.controller;


import com.thoughtworks.skillpilot.dto.UserDTO;
import com.thoughtworks.skillpilot.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/register/admin")
    public ResponseEntity<String> registerAdmin(@Valid @RequestBody UserDTO userDTO) {
        userService.addAdmin(userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail());
        return ResponseEntity.ok("Admin registered successfully!");
    }

    @PostMapping("/register/learner")
    public ResponseEntity<String> registerLearner(@Valid @RequestBody UserDTO userDTO) {
        userService.addLearner(userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail());
        return ResponseEntity.ok("Learner registered successfully!");
    }
}