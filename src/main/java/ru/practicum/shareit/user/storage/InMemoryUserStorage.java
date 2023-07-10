package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 0L;

    private long countId() {
        return ++id;
    }

    @Override
    public User add(User user) {
        user.setId(countId());
        users.put(user.getId(), user);
        log.debug("Создан пользователь с id: {}", user.getId());
        return user;
    }

    @Override
    public void deleteUserById(Long userId) {
        users.remove(userId);
        log.debug("Пользователь с id: {} удален", userId);
    }

    @Override
    public List<User> getAllUsers() {
        log.debug("Возвращен список пользователей");
        return new ArrayList<>(users.values());
    }

    @Override
    public User update(User newUser) {
        if (!users.containsKey(newUser.getId())) {
            throw new NotFoundException(String.format("Не найден пользователь с id: %s", newUser.getId()));
        }
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User getUserById(Long userId) {
        log.debug("Возвращен пользователь с id: {}", userId);
        return users.get(userId);
    }

    @Override
    public boolean isExistEmail(String email) {
        return users.values().stream().map(User::getEmail).anyMatch(email::equals);
    }

    @Override
    public boolean userExists(long id) {
        return users.containsKey(id);
    }
}
