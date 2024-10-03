package com.alpineblue.waveme_backend.crud.service.impl;

import com.alpineblue.waveme_backend.crud.dto.UserDto;
import com.alpineblue.waveme_backend.crud.entity.User;
import com.alpineblue.waveme_backend.crud.exception.ResourceNotFoundException;
import com.alpineblue.waveme_backend.crud.mapper.UserMapper;
import com.alpineblue.waveme_backend.crud.repository.UserRepository;
import com.alpineblue.waveme_backend.crud.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        User savedUser = userRepository.save(user);

        return UserMapper.mapToUserDto(savedUser);
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found.."));

        return UserMapper.mapToUserDto(user);
    }

    @Override
    public List<User> getUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDto updateUser(Long userId, String pseudo, String email, String password, String profileImg) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee is not found with id: " + userId));

        user.setPseudo(pseudo);
        user.setEmail(email);
        user.setProfileImg(profileImg);
        user.setPassword(password);

        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto deleteUser(Long userId) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException("User is not found with id: " + userId));

        userRepository.delete(user);

        return UserMapper.mapToUserDto(user);
    }
}
