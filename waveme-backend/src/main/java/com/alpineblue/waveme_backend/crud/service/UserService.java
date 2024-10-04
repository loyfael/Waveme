package com.alpineblue.waveme_backend.crud.service;

import com.alpineblue.waveme_backend.crud.dto.UserDto;
import com.alpineblue.waveme_backend.crud.entity.User;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserById(Long userId);
    List<User> getUsers();
    UserDto updateUser(Long userId, String pseudo, String email, String password, String profileImg);
    UserDto deleteUser(Long userId);
}
