package ru.practicum.shareit.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@RequiredArgsConstructor
public class ItemRequest {
    //уникальный идентификатор запроса.
    private Long id;
    //текст запроса, содержащий описание требуемой вещи.
    private String description;
    //пользователь, создавший запрос.
    private User requestor;
    //дата и время создания запроса.
    private LocalDate created;
}