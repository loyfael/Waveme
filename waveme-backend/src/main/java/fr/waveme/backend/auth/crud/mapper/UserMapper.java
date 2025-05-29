package fr.waveme.backend.auth.crud.mapper;

import fr.waveme.backend.auth.crud.dto.UserDto;
import fr.waveme.backend.auth.crud.models.User;

public class UserMapper {
    public static UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getPseudo(),
                user.getEmail(),
                user.getProfileImg(),
                user.getPassword()
        );
    }

    public static User mapToUser(UserDto userDto) {
        User user = new User();

        user.setId(userDto.getId());
        user.setPseudo(userDto.getPseudo());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setProfileImg(userDto.getProfileImg());

        return user;
    }
}