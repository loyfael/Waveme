package fr.waveme.backend.controller;

import fr.waveme.backend.crud.dto.UserDto;
import fr.waveme.backend.crud.entity.User;
import fr.waveme.backend.crud.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class TestController {
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto)
    {
        UserDto savedUser = userService.createUser(userDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto)
    {
        UserDto savedUser = userService.updateUser(
                userDto.getId(), userDto.getPseudo(), userDto.getEmail(), userDto.getPassword(), userDto.getProfileImg()
        );
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long userId)
    {
        UserDto userDto = userService.getUserById(userId);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("all")
    public ResponseEntity<List<User>> getUsers()
    {
        List<User> userDto = userService.getUsers();
        return ResponseEntity.ok(userDto);
    }

    public ResponseEntity<UserDto> deleteUser(@PathVariable("id") Long userId) {
        UserDto userDto = userService.deleteUser(userId);
        return ResponseEntity.ok(userDto);
    }
}