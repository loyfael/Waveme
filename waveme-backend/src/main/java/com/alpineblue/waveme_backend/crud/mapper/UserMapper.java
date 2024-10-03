package com.alpineblue.waveme_backend.crud.mapper;

import com.alpineblue.waveme_backend.crud.dto.UserDto;
import com.alpineblue.waveme_backend.crud.entity.User;

public class UserMapper {
    public static UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUserId(),
                user.getPseudo(),
                user.getEmail(),
                user.getProfileImg(),
                user.getPost()
        );
    }

    public static User mapToUser(UserDto userDto) {
        User user = new User();

        user.setId(userDto.getId());
        user.setUserId(userDto.getUserId());
        user.setPseudo(userDto.getPseudo());
        user.setEmail(userDto.getEmail());
        user.setProfileImg(userDto.getProfileImg());
        user.setPost(userDto.getPost());

        return user;
    }
}