package ru.practicum.shareit.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@Data
@RequiredArgsConstructor
@Entity
@Table(name = "booking")
public class Booking {
    //уникальный идентификатор бронирования.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private long id;
    //дата и время начала бронирования.
    @Column(name = "start_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate start;
    //дата и время конца бронирования.
    @Column(name = "end_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate end;
    //вещь, которую пользователь бронирует.
    @ManyToOne
    @JoinColumn(name = "item_id")
    private long itemId;
    //пользователь, который осуществляет бронирование.
    @ManyToOne
    @JoinColumn(name = "booker_id", referencedColumnName = "user_id")
    private long bookerId;
    //статус бронирования
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}