package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryItemStorage implements ItemStorage {
    protected final Map<Long, Item> items = new HashMap<>();
    private long id = 0L;

    private long countId() {
        return ++id;
    }

    @Override
    public Item add(Item item) {
        item.setId(countId());
        items.put(item.getId(), item);
        log.debug("Добавлена вещ: {}", item);
        return item;
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        log.debug("Данные по вещи: {} обновлены пользователем с id: {}", item, item.getId());
        return item;
    }

    @Override
    public Item getById(long itemId) {
        log.debug("Вернули вещ с id: {}", itemId);
        return items.get(itemId);
    }

    @Override
    public List<Item> getOwnerItems(long userId) {
        log.debug("Вернули список вещей пользователя с id: {}", userId);
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItems(String text) {
        System.out.println("-------------------");
        System.out.println(items.toString());
        System.out.println("-------------------");

        return items.values().stream()
                .filter(item -> (item.getDescription().toLowerCase().contains(text.toLowerCase())
                        || item.getName().toLowerCase().contains(text.toLowerCase()))
                        && item.getAvailable())
                .collect(Collectors.toList());
    }
}