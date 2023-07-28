package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public User add(User user) {
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteUserById(long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public User update(long userId, UserDto userDto) {
        User newUser = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь с id: %s не найден", userId)));

        if (userDto.getName() != null) {
            newUser.setName(userDto.getName());
        }

        if (userDto.getEmail() != null && !userDto.getEmail().equals(newUser.getEmail())) {
            newUser.setEmail(userDto.getEmail());
        }

        return newUser;
    }

    @Override
    public User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException(String.format("Пользователь с id: %s не найден", userId)));
    }
}