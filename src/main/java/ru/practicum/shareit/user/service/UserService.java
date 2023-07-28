package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User add(User user);

    void deleteUserById(long userId);

    List<User> getAllUsers();

    User update(long userId, UserDto userDto);

    User getUserById(long userId);
}