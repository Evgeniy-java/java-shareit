package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public User update(User user, Long userId) {
        User updateUser = users.get(userId);

        String name = user.getName();
        String email = user.getEmail();

        updateUser.setName(name != null && !name.isBlank() ? name : updateUser.getName());

        if (email != null && !email.isBlank()) {
            boolean emailExist = getAllUsers().stream()
                    .filter(u -> !u.getId().equals(userId))
                    .anyMatch(u -> email.equals(u.getEmail()));
            if (emailExist) {
                throw new ConflictException(String.format("email: %s уже существует", email));
            }
            updateUser.setEmail(email);
        }

        users.put(userId, updateUser);
        return updateUser;
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