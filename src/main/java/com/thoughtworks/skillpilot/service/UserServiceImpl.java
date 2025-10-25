package com.thoughtworks.skillpilot.service;


import com.thoughtworks.skillpilot.DTO.UserDTO;
import com.thoughtworks.skillpilot.dto.UserResponseDTO;
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

    public UserResponseDTO getUser(UserDTO userToLogin) {

        if (userRepository.findByUsernameAndPassword(userToLogin.getUsername(), userToLogin.getPassword()).isPresent()) {
            User user = userRepository.findByUsernameAndPassword(userToLogin.getUsername(), userToLogin.getPassword()).get();
            return mapUsertouserdto(user);
        } else {
            return null;
        }
    }

    private UserResponseDTO mapUsertouserdto(User user) {
        UserResponseDTO userDto = new UserResponseDTO();
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());
        userDto.setRole(user.getRole());
        return userDto;
    }


}