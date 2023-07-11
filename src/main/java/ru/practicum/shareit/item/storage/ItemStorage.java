package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item add(Item item);

    Item update(long userId, Item item);

    Item getById(long itemId);

    List<Item> getOwnerItems(long userId);

    List<Item> searchItems(String text);
}