package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

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
            log.debug("Некорректный id: {} пользователя", userId);
            throw new NotFoundException(String.format("Некорректный id: %s пользователя", userId));
        }
        User user = userStorage.getUserById(userId);
        Item item = ItemMapper.toItem(itemDto);

        item.setOwner(user);

        return ItemMapper.toItemDto(itemStorage.add(item));
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        Item item = itemStorage.getById(itemId);

        if (!item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Чужую вещ нельзя редактировать");
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

        itemStorage.update(userId, item);

        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        Item item = itemStorage.getById(itemId);

        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getOwnerItems(Long userId) {
        List<Item> items = itemStorage.getOwnerItems(userId);
        return ItemMapper.itemToDto(items);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        return itemStorage.searchItems(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
