package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "users")
public class User {
    //уникальный идентификатор пользователя.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //имя или логин пользователя.
    @NotBlank
    @Column(nullable = false, length = 100)
    private String name;

    /*адрес электронной почты (учтите, что два пользователя не могут
      иметь одинаковый адрес электронной почты).*/
    @Email
    @NotBlank
    @Column(unique = true, nullable = false, length = 60)
    private String email;
}