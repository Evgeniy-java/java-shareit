package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDto {
    private long id;

    @NotBlank(message = "Не заполнено название вещи")
    private String name;

    @NotBlank(message = "Не заполнено описание вещи")
    @Size(max = 240, message = "В описании не более 240 символов")
    private String description;

    @NotNull(message = "Не проставлен статус вещи")
    private Boolean available;

    private ItemRequest request;
}