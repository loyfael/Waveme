package fr.waveme.backend.crud.mapper;

import fr.waveme.backend.crud.dto.UserDto;
import fr.waveme.backend.crud.entity.User;

public class UserMapper {
    public static UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUserId(),
                user.getPseudo(),
                user.getEmail(),
                user.getProfileImg(),
                user.getPosts()
        );
    }

    public static User mapToUser(UserDto userDto) {
        User user = new User();

        user.setId(userDto.getId());
        user.setUserId(userDto.getUserId());
        user.setPseudo(userDto.getPseudo());
        user.setEmail(userDto.getEmail());
        user.setProfileImg(userDto.getProfileImg());
        user.setPosts(userDto.getPost());

        return user;
    }
}