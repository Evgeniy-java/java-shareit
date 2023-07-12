package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemDto add(Long userId, ItemDto itemDto) {
        if (!userStorage.userExists(userId)) {
            log.debug("Пользователь с id: {} не найден", userId);
            throw new NotFoundException(String.format("Пользователь с id: %s не найден", userId));
        }
        User user = userStorage.getUserById(userId);
        Item item = ItemMapper.toItem(itemDto, user);

        item.setOwner(user);

        return ItemMapper.toItemDto(itemStorage.add(item));
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        Item item = itemStorage.getById(itemId);

        if (item == null) {
            throw new NotFoundException(String.format("Вещ с id: %s не найдена", itemId));
        }

        if (!item.getOwner().getId().equals(userId)) {
            throw new ForbiddenException("Чужую вещ запрещено редактировать");
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        return ItemMapper.toItemDto(itemStorage.update(item));
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        return ItemMapper.toItemDto(itemStorage.getById(itemId));
    }

    @Override
    public List<ItemDto> getOwnerItems(Long userId) {
        return ItemMapper.itemToDto(itemStorage.getOwnerItems(userId));
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemStorage.searchItems(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}