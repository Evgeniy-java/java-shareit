package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                       @Valid @RequestBody ItemDto itemDto) {
        log.debug("Получен Post /items запрос " +
                "на добавление вещи: {} пользователем с id: {}", itemDto, userId);
        return itemService.add(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                          @PathVariable("itemId") @Positive Long itemId,
                          @RequestBody ItemDto itemDto) {
        log.debug("Получен Patch /items/{itemId} запрос " +
                "на обновление вещи: {} пользователем с id: {}", itemDto, userId);
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable("itemId") @Positive Long itemId) {
        log.debug("Получен Get /items/{itemId} запрос " +
                "на получение вещи по id: {}", itemId);
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getOwnerItems(@RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        log.debug("Получен Get /items/{itemId} запрос " +
                "на получение всех вещей пользователя с id: {}", userId);
        return itemService.getOwnerItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam(required = false) String text) {
        log.debug("Получен Get /items/search запрос " +
                "на поиск вещи по тексту: {}", text);
        return itemService.searchItems(text);
    }
}