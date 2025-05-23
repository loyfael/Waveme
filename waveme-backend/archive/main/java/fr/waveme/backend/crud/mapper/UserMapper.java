package fr.waveme.backend.crud.mapper;

import fr.waveme.backend.crud.dto.UserDto;
import fr.waveme.backend.crud.models.User;

public class UserMapper {
    public static UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getPseudo(),
                user.getEmail(),
                user.getProfileImg(),
                user.getPassword(),
                user.getPosts()
        );
    }

    public static User mapToUser(UserDto userDto) {
        User user = new User();

        user.setId(userDto.getId());
        user.setPseudo(userDto.getPseudo());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setProfileImg(userDto.getProfileImg());
        user.setPosts(userDto.getPosts());

        return user;
    }
}