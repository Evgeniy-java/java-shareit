package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingBriefDto;

import java.util.List;

public interface BookingService {
    BookingDto add(BookingBriefDto bookingBriefDto, Long userId);

    BookingDto approve(Long bookingId, Long userId, Boolean approved);

    List<BookingDto> getAllByUserId(Long userId, String state);

    List<BookingDto> getAllByOwnerId(Long ownerId, String state);

    BookingDto getById(Long itemId, Long userId);

}