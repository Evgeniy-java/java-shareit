package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Item {
    //уникальный идентификатор вещи.
    private Long id;
    //краткое название.
    @NotBlank(message = "Не заполнено название вещи")
    private String name;
    //развёрнутое описание.
    @NotBlank(message = "Не заполнено описание вещи")
    @Size(max = 240, message = "В описании не более 240 символов")
    private String description;
    //статус о том, доступна или нет вещь для аренды.
    @NotNull(message = "Не проставлен статус вещи")
    private Boolean available;
    //владелец вещи.
    private User owner;
    /*если вещь была создана по запросу другого пользователя, то в этом
      поле будет храниться ссылка на соответствующий запрос*/
    private ItemRequest request;
}