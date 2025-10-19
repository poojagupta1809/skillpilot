package com.thoughtworks.skillpilot.service;


import com.thoughtworks.skillpilot.exception.UserAlreadyExists;
import com.thoughtworks.skillpilot.exception.ValidationExceptionMessages;
import com.thoughtworks.skillpilot.model.RoleType;
import com.thoughtworks.skillpilot.model.User;
import com.thoughtworks.skillpilot.repository.UserRepository;

import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {




    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registerNewUser(String username, String password, String email, String roleName) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExists(ValidationExceptionMessages.USERNAME_ALREADY_TAKEN);
        }

        if(userRepository.findByUserEmail(email).isPresent()) {
            throw new UserAlreadyExists(ValidationExceptionMessages.USER_WITH_THIS_EMAIL_ALREADY_EXISTS);
        }

        User user = new User(username, password);


        RoleType roleType = null;
        try {
            roleType = RoleType.valueOf(roleName.toUpperCase());
            user.setRole(roleType.name());

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role name: " + roleName);
        }


        user.setEmail(email);

        return userRepository.save(user);
    }


}