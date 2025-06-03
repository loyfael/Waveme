package fr.waveme.backend.auth.crud.mapper;

import fr.waveme.backend.auth.crud.dto.UserDto;
import fr.waveme.backend.auth.crud.models.User;

/**
 * UserMapper is a utility class that provides methods to convert between User and UserDto objects.
 * It contains static methods to map User to UserDto and vice versa.
 * This is useful for separating the data representation used in the application from the database entity.
 */
public class UserMapper {
    public static UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getPseudo(),
                user.getEmail(),
                user.getPassword()
        );
    }

    public static User mapToUser(UserDto userDto) {
        User user = new User();

        user.setId(userDto.getId());
        user.setPseudo(userDto.getPseudo());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());

        return user;
    }
}