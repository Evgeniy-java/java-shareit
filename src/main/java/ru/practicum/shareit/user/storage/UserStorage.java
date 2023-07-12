package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    User add(User user);

    void deleteUserById(Long userId);

    List<User> getAllUsers();

    User update(User user, Long userId);

    User getUserById(Long userId);

    boolean isExistEmail(String email);

    boolean userExists(long id);
}