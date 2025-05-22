package fr.waveme.crud.service.impl;

import fr.waveme.crud.dto.UserDto;
import fr.waveme.crud.mapper.UserMapper;
import fr.waveme.crud.models.User;
import fr.waveme.crud.repository.UserRepository;
import fr.waveme.crud.service.UserService;
import fr.waveme.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
                .orElseThrow(() -> new ResourceNotFoundException("User is not found with id: " + userId));

        user.setPseudo(pseudo);
        user.setEmail(email);
        user.setPassword(password);

        User updatedUser = userRepository.save(user);

        return UserMapper.mapToUserDto(updatedUser);
    }

    @Override
    public UserDto deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User is not found with id: " + userId));

        userRepository.delete(user);

        return UserMapper.mapToUserDto(user);
    }
}

