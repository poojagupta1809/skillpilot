package com.thoughtworks.skillpilot.service;

import com.thoughtworks.skillpilot.model.User;

public interface UserService {
    User registerNewUser(String username, String password, String email, String roleName);

    User addAdmin(String username, String password, String email);

    User addLearner(String username, String password, String email);
}
