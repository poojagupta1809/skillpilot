package com.thoughtworks.skillpilot.service;

import com.thoughtworks.skillpilot.dto.UserDTO;
import com.thoughtworks.skillpilot.dto.UserResponseDTO;
import com.thoughtworks.skillpilot.model.User;

public interface UserService {

  User registerNewUser(String username, String password, String email, String roleName);

  public UserResponseDTO getUser(UserDTO user);
}
