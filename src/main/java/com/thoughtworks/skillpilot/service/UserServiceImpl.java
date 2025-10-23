package com.thoughtworks.skillpilot.service;


import com.thoughtworks.skillpilot.dto.UserDTO;
import com.thoughtworks.skillpilot.exception.InvalidRoleException;
import com.thoughtworks.skillpilot.exception.UserAlreadyExistsException;
import com.thoughtworks.skillpilot.exception.ValidationExceptionMessages;
import com.thoughtworks.skillpilot.model.RoleType;
import com.thoughtworks.skillpilot.model.User;
import com.thoughtworks.skillpilot.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registerNewUser(String username, String password, String email, String roleName) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException(ValidationExceptionMessages.USERNAME_ALREADY_TAKEN);
        }

        if (userRepository.findByUserEmail(email).isPresent()) {
            throw new UserAlreadyExistsException(ValidationExceptionMessages.USER_WITH_THIS_EMAIL_ALREADY_EXISTS);
        }

        User user = new User(username, password);

        RoleType roleType;
        try {
            roleType = RoleType.valueOf(roleName.toUpperCase());
            user.setRole(roleType.name());

        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException(ValidationExceptionMessages.INVALID_ROLE_NAME + roleName);
        }


        user.setEmail(email);

        return userRepository.save(user);
    }

    public boolean validateUser(UserDTO profile) {

         return userRepository.findByUsernameAndPassword(profile.getUsername(), profile.getPassword()).isPresent();
    }


}