package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingBriefDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto add(@Valid @RequestBody BookingBriefDto bookingBriefDto,
                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("Получен Post/bookings запросна на бронирование вещи: " +
                "{} пользователем с id = {}", bookingBriefDto, userId);
        return bookingService.add(bookingBriefDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestParam Boolean approved) {
        log.debug("Получен Patch/bookings/{bookingId} запрос на подтверждение или отклонение " +
                "бронирования с id: {}", bookingId);
        return bookingService.approve(bookingId, userId, approved);
    }

    @GetMapping
    public List<BookingDto> getAllByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestParam(defaultValue = "ALL") String state) {
        log.debug("Получен Get/bookings/owner запрос на получение " +
                "списка всех бронирований текущего пользователя с id: {}", userId);
        return bookingService.getAllByUserId(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwnerId(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                          @RequestParam(defaultValue = "ALL") String state) {
        log.debug("Получен Get/bookings/owner запрос на получение " +
                "списка бронирований для всех вещей текущего пользователя с id: {}", ownerId);
        return bookingService.getAllByOwnerId(ownerId, state);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("BookingController: GET getById, userId = {}; bookingId = {}", userId, bookingId);
        return bookingService.getById(bookingId, userId);
    }
}