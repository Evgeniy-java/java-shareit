package ru.practicum.shareit.user.model;

import lombok.*;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class User {
    //уникальный идентификатор пользователя.
    private Long id;
    //имя или логин пользователя.
    private String name;
    /*адрес электронной почты (учтите, что два пользователя не могут
      иметь одинаковый адрес электронной почты).*/
    private String email;
}