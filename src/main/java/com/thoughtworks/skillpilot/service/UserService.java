package com.thoughtworks.skillpilot.service;


import com.thoughtworks.skillpilot.model.User;
import com.thoughtworks.skillpilot.repository.RoleRepository;
import com.thoughtworks.skillpilot.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User registerNewUser(String username, String password, String roleName) {
        return null;
    }

    public User addAdmin(String username, String password) {
        return registerNewUser(username, password, "ADMIN");
    }

    public User addLearner(String username, String password) {
        return registerNewUser(username, password, "LEARNER");
    }
}