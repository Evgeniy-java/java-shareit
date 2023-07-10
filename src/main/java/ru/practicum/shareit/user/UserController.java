package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto add(@Valid @RequestBody UserDto userDto) {
        log.debug("Получен Post /users запрос на добавлен пользователя: {}", userDto);
        return userService.add(userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable("userId") Long userId) {
        log.debug("Получен Delete /users/{userId} запрос на удаление пользователя по id: {}", userId);
        userService.deleteUserById(userId);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.debug("Получен Get /users запрос на получение всех пользователей");
        return userService.getAllUsers();
    }

    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody UserDto userDto, @PathVariable("userId") Long userId) {
        log.debug("Получен Patch /users/{userId} запрос на обновление пользователя с id: {}", userId);
        return userService.update(userDto, userId);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable("userId") Long userId) {
        log.debug("Получен Get /users/{userId} запрос на получение пользователя по id: {}", userId);
        return userService.getUserById(userId);
    }
}
