package fr.waveme.crud.service;

import fr.waveme.crud.models.User;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserById(Long userId);
    List<User> getUsers();
    UserDto updateUser(Long id, String pseudo, String email, String password, String profileImg);
    UserDto deleteUser(Long userId);
}
