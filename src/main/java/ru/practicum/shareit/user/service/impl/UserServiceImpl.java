package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public UserDto add(UserDto userDto) {
        User newUser = UserMapper.toUser(userDto);
        if (userStorage.isExistEmail(userDto.getEmail())) {
            throw new ConflictException(String.format("Пользователь с email: %s уже существует", userDto.getEmail()));
        }

        return UserMapper.toUserDto(userStorage.add(newUser));
    }

    @Override
    public void deleteUserById(Long userId) {
        userStorage.deleteUserById(userId);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> list = new ArrayList<>();
        for (User user : userStorage.getAllUsers()) {
            UserDto userDto = UserMapper.toUserDto(user);
            list.add(userDto);
        }
        return list;
    }

    @Override
    public UserDto update(UserDto userDto, Long userId) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userStorage.update(user, userId));
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userStorage.getUserById(userId);
        return UserMapper.toUserDto(user);
    }
}