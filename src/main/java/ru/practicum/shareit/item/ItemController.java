package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                       @Valid @RequestBody ItemDto itemDto) {
        log.debug("Получен Post /items запрос " +
                "на добавление вещи: {} пользователем с id: {}", itemDto, userId);
        return itemService.add(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto,
                          @PathVariable("itemId") @Positive Long itemId,
                          @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        log.debug("Получен Patch /items/{itemId} запрос " +
                "на обновление вещи: {} пользователем с id: {}", itemDto, userId);
        return itemService.update(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId,
                               @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("Получен Get /items/{itemId} запрос " +
                "на получение вещи по id: {}", itemId);
        return itemService.getItemById(itemId, userId);
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

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId,
                                 @Valid @RequestBody CommentDto commentDto) {
        log.info("ItemController: POST createComment, userId = {}", userId);
        return itemService.addComment(itemId, userId, commentDto);
    }
}