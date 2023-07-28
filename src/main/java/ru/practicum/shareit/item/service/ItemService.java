package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto add(ItemDto item, Long userId);

    ItemDto update(ItemDto item, Long itemId, Long userId);

    ItemDto getItemById(Long itemId, Long userId);

    List<ItemDto> getOwnerItems(Long userId);

    List<ItemDto> searchItems(String request);

    CommentDto addComment(Long itemId, Long userId, CommentDto commentDto);
}