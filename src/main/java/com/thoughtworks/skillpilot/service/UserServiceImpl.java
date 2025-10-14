package com.thoughtworks.skillpilot.service;


import com.thoughtworks.skillpilot.exception.UserAlreadyExists;
import com.thoughtworks.skillpilot.model.Role;
import com.thoughtworks.skillpilot.model.RoleType;
import com.thoughtworks.skillpilot.model.User;
import com.thoughtworks.skillpilot.repository.RoleRepository;
import com.thoughtworks.skillpilot.repository.UserRepository;

import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public User registerNewUser(String username, String password, String email, String roleName) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExists("Username already taken!");
        }

        if(userRepository.findByUserEmail(email).isPresent()) {
            throw new UserAlreadyExists("User with this email already exists.");
        }

        User user = new User(username, password);
        Set<Role> roles = new HashSet<>();

        RoleType roleType;
        try {
            roleType = RoleType.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role name: " + roleName);
        }

        Optional<Role> roleOptional = roleRepository.findByName(roleType);
        if (roleOptional.isPresent()) {
            roles.add(roleOptional.get());
        } else {
            roles.add(new Role(roleType));
        }

        user.setRoles(roles);
        user.setEmail(email);

        return userRepository.save(user);
    }

    @Override
    public User addAdmin(String username, String password, String email) {
        return registerNewUser(username, password,email, "ADMIN");
    }

    @Override
    public User addLearner(String username, String password, String email) {
        return registerNewUser(username, password, email,"LEARNER");
    }
}