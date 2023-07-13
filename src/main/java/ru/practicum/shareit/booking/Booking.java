package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@Data
@RequiredArgsConstructor
public class Booking {
    //уникальный идентификатор бронирования.
    private long id;
    //дата и время начала бронирования.
    private LocalDate start;
    //дата и время конца бронирования.
    private LocalDate end;
    //вещь, которую пользователь бронирует.
    private long itemId;
    //пользователь, который осуществляет бронирование.
    private long bookerId;
    //статус бронирования
    private BookingStatus status;
}