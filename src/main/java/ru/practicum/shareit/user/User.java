package ru.practicum.shareit.user;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * TODO Sprint add-controllers.
 */
@Data
@RequiredArgsConstructor
public class User {
    //уникальный идентификатор пользователя.
    private int id;
    //имя или логин пользователя.
    private String name;
    /*адрес электронной почты (учтите, что два пользователя не могут
      иметь одинаковый адрес электронной почты).*/
    private String email;
}