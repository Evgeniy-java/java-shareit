package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-controllers.
 */
public class ItemDto{
    //уникальный идентификатор вещи.
    private int id;
    //краткое название.
    private String name;
    //развёрнутое описание.
    private String description;
    //статус о том, доступна или нет вещь для аренды.
    private boolean available;
    //владелец вещи.
    private String owner;
    /*если вещь была создана по запросу другого пользователя, то в этом
      поле будет храниться ссылка на соответствующий запрос*/
    private User request;

    public ItemDto(String name, String description, boolean available, Integer integer) {
    }
}