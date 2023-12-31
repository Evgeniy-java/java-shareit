package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item {
    //уникальный идентификатор вещи.
    private Long id;
    //краткое название.
    private String name;
    //развёрнутое описание.
    private String description;
    //статус о том, доступна или нет вещь для аренды.
    private Boolean available;
    //владелец вещи.
    private User owner;
    /*если вещь была создана по запросу другого пользователя, то в этом
      поле будет храниться ссылка на соответствующий запрос*/
    private ItemRequest request;
}