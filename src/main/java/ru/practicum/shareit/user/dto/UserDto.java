package ru.practicum.shareit.user.dto;

/**
 * TODO Sprint add-controllers.
 */
public class UserDto {
    //уникальный идентификатор пользователя.
    private int id;
    //имя или логин пользователя.
    private String name;
    /*адрес электронной почты (учтите, что два пользователя не могут
      иметь одинаковый адрес электронной почты).*/
    private String email;

    public UserDto(String name, String email) {
    }
}