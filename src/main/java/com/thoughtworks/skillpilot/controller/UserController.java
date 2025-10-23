// UserController.java
package com.thoughtworks.skillpilot.controller;


import com.thoughtworks.skillpilot.dto.UserDTO;
import com.thoughtworks.skillpilot.service.UserServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceImpl userService;


    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO) {
        userService.registerNewUser(userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail(),userDTO.getRole());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<?> validate(@RequestBody UserDTO userDTO)
    {
        boolean validatedUser= userService.validateUser(userDTO);

        if (validatedUser)
        {
            HashMap<String,String> tokenMap=new HashMap<>();
            String generatedToken=generateToken(userDTO);
            tokenMap.put("token",generatedToken);
            return new ResponseEntity<>(tokenMap,HttpStatus.OK);

        }
        else
        {
            return new ResponseEntity<>("Not Authorized", HttpStatus.UNAUTHORIZED);
        }
    }


    private String generateToken(UserDTO userObj)
    {

        long expiry=10_000_00;

        return Jwts.builder().setSubject( userObj.getEmail())
                .setAudience(userObj.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ expiry))
                .signWith(SignatureAlgorithm.HS256, "twteamkey")
                .compact();



    }

}