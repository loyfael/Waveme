package fr.waveme.backend.auth.crud.service;

import fr.waveme.backend.auth.crud.dto.UserDto;
import fr.waveme.backend.auth.crud.models.User;

import java.util.List;

/**
 * UserService is an interface that defines the contract for user-related operations in the application.
 * It provides methods for creating, retrieving, updating, and deleting users.
 * This service is used to manage user data and business logic related to user operations.
 */
public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserById(Long userId);
    List<User> getUsers();
    UserDto updateUser(Long id, String pseudo, String email, String password);
    UserDto deleteUser(Long userId);
}
