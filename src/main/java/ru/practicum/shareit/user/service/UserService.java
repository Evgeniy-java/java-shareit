package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto add(UserDto userDto);

    void deleteUserById(Long userId);

    List<UserDto> getAllUsers();

    UserDto update(UserDto userDto, Long userId);

    UserDto getUserById(Long userId);

}