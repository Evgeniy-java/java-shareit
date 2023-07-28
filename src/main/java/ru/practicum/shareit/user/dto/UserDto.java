package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserDto {
    //уникальный идентификатор пользователя.
    private Long id;
    //имя или логин пользователя.
    @NotBlank(message = "Не заполнено имя пользователя")
    private String name;
    /*адрес электронной почты (учтите, что два пользователя не могут
      иметь одинаковый адрес электронной почты).*/
    @NotBlank(message = "Email не заполнен")
    @Email(message = "Некорректный email")
    private String email;
}