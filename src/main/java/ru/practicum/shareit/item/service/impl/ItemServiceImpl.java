package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingBriefDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public ItemDto add(ItemDto itemDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь с id: %s не найден", userId)));

        Item item = ItemMapper.toItem(itemDto, user);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Transactional
    @Override
    public ItemDto update(ItemDto itemDto, Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещ с id: %s не найдена", itemId)));

        if (!item.getOwner().getId().equals(userId)) {
            throw new ForbiddenException(String.format("Пользователь с id: %s не владелец вещи: %s",
                    item.getOwner().getId(), itemDto));
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

        return ItemMapper.toItemDto(item);
    }

    @Transactional(readOnly = true)
    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException(String.format("Вещ с id: %s не найдена", itemId)));

        ItemDto itemDto = ItemMapper.toItemDto(item);

        itemDto.setComments(commentRepository.findAllByItemId(itemId)
                .stream().map(CommentMapper::toCommentDto).collect(toList()));

        if (!item.getOwner().getId().equals(userId)) {
            return itemDto;
        }

        List<Booking> lastBooking = bookingRepository.findTopBookingByItemIdAndEndIsBeforeAndStatusIs(
                itemId, LocalDateTime.now(), BookingStatus.APPROVED, Sort.by(DESC, "end"));
        List<Booking> nextBooking = bookingRepository.findTopBookingByItemIdAndEndIsAfterAndStatusIs(
                itemId, LocalDateTime.now(), BookingStatus.APPROVED, Sort.by(Sort.Direction.ASC, "end"));

        if (lastBooking.isEmpty() && !nextBooking.isEmpty()) {
            itemDto.setLastBooking(BookingMapper.toBookingBriefDto(nextBooking.get(0)));
            itemDto.setNextBooking(null);
        } else if (!lastBooking.isEmpty() && !nextBooking.isEmpty()) {
            itemDto.setLastBooking(BookingMapper.toBookingBriefDto(lastBooking.get(0)));
            itemDto.setNextBooking(BookingMapper.toBookingBriefDto(nextBooking.get(0)));
        }

        return itemDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> getOwnerItems(Long userId) {
        Set<Item> items = new HashSet<>(itemRepository.findAllByOwnerId(userId));
        if (items.isEmpty()) {
            return new ArrayList<>();
        }
        Booking lastBooking;
        Booking nextBooking;
        LocalDateTime now = LocalDateTime.now();

        List<ItemDto> itemDtoList = new ArrayList<>();
        Map<Item, List<Comment>> comments = commentRepository.findByItemIn(items, Sort.by(DESC, "created"))
                .stream()
                .collect(groupingBy(Comment::getItem, toList()));
        Map<Item, List<Booking>> bookingMap = bookingRepository.findByItemInAndStatusIs(items,
                BookingStatus.APPROVED, Sort.by(DESC, "end"))
                .stream()
                .collect(groupingBy(Booking::getItem, toList()));
        for (Item item : items) {
            lastBooking = null;
            nextBooking = null;
            ItemDto itemDto = ItemMapper.toItemDto(item);

            if (comments.get(item) != null) {
                itemDto.setComments(comments.get(item).stream()
                        .map(CommentMapper::toCommentDto).collect(Collectors.toList()));
            }

            if (bookingMap.get(item) != null) {
                log.info("Now = " + now);
                for (Booking booking : bookingMap.get(item)) {
                    log.info("bookingId = " + booking.getId());
                    log.info("bookingStart = " + booking.getStart());
                    log.info("bookingEnd = " + booking.getEnd());
                    if (lastBooking == null && booking.getEnd().isBefore(now)) {
                        lastBooking = booking;
                    } else if (booking.getEnd().isBefore(now) &&
                            booking.getEnd().isAfter(Objects.requireNonNull(lastBooking).getEnd())) {
                        lastBooking = booking;
                    }

                    if (nextBooking == null && booking.getStart().isAfter(now)) {
                        nextBooking = booking;
                    } else if (booking.getStart().isAfter(now) &&
                            booking.getStart().isBefore(Objects.requireNonNull(nextBooking).getStart())) {
                        nextBooking = booking;
                    }

                }
                if (lastBooking == null && nextBooking != null) {
                    lastBooking = nextBooking;
                    nextBooking = null;
                }
            }
            itemDto.setLastBooking(lastBooking == null ? new BookingBriefDto() : BookingMapper.toBookingBriefDto(lastBooking));
            itemDto.setNextBooking(nextBooking == null ? new BookingBriefDto() : BookingMapper.toBookingBriefDto(nextBooking));
            itemDtoList.add(itemDto);
        }

        itemDtoList.sort(Comparator.comparing(o -> o.getLastBooking().getStart(),
                Comparator.nullsLast(Comparator.reverseOrder())));
        for (ItemDto itemDto : itemDtoList) {
            if (itemDto.getLastBooking().getBookerId() == null) {
                itemDto.setLastBooking(null);
            }
            if (itemDto.getNextBooking().getBookerId() == null) {
                itemDto.setNextBooking(null);
            }
        }
        return itemDtoList;
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }

        return itemRepository.findAll().stream()
                .map(ItemMapper::toItemDto)
                .filter(i -> i.getAvailable().equals(true))
                .filter(i -> i.getName().toLowerCase().contains(text.toLowerCase()) ||
                        i.getDescription().toLowerCase().contains(text.toLowerCase()))
                .collect(toList());
    }

    @Override
    public CommentDto addComment(Long itemId, Long userId, CommentDto commentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id: %s не найден", userId)));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещ с id: %s не найдена", itemId)));
        if (bookingRepository.findAllByBookerIdAndItemIdAndStatusEqualsAndEndIsBefore(userId, itemId, BookingStatus.APPROVED,
                LocalDateTime.now()).isEmpty()) {
            throw new BadRequestException("Не корректный комментарий");
        }
        Comment comment = CommentMapper.toCommentModel(commentDto);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);

        return CommentMapper.toCommentDto(comment);
    }
}