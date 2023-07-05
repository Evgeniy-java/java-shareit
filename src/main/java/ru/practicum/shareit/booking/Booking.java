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
    private int id;
    //дата и время начала бронирования.
    private LocalDate start;
    //дата и время конца бронирования.
    private LocalDate end;
    //вещь, которую пользователь бронирует.
    private String item;
    //пользователь, который осуществляет бронирование.
    private String booker;
    //статус бронирования
    private BookingStatus status;
}