package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingBriefDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    protected static final Sort SORT = Sort.by(Sort.Direction.DESC, "start");

    @Transactional
    @Override
    public BookingDto add(BookingBriefDto bookingBriefDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь с id: %s не найден", userId)));

        long itemId = bookingBriefDto.getItemId();
        Item item = itemRepository.findById(bookingBriefDto.getItemId()).orElseThrow(() ->
                new NotFoundException(String.format("Вещ с id: %s не найдена", itemId)));

        if (item.getOwner().getId().equals(userId)) {
            throw new NotFoundException(String.format("Пользователь с id: %s не может забронировать свою вещ)", userId));
        }
        if (!item.getAvailable()) {
            throw new BadRequestException(String.format("Вещ с id: %s не доступна", item.getId()));
        }
        Booking booking = BookingMapper.toBooking(bookingBriefDto);
        if (booking.getEnd().isBefore(booking.getStart()) || booking.getEnd().isEqual(booking.getStart())) {
            throw new BadRequestException("Не корректный период брони");
        }
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        bookingRepository.save(booking);

        return BookingMapper.toBookingDto(booking);
    }

    @Transactional
    @Override
    public BookingDto approve(Long bookingId, Long userId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException(String.format("Некорректный id: %s брони", bookingId)));

        if (!userId.equals(booking.getItem().getOwner().getId())) {
            throw new NotFoundException(String.format("Пользователь с id: %s не владелец вещи с id: %s", userId, booking.getItem().getId()));
        }
        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new BadRequestException("Бронирование уже имеет статус: одобрено или нет");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        bookingRepository.save(booking);

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllByUserId(Long userId, String state) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь с id: %s не найден", userId)));

        List<Booking> bookingDtoList = new ArrayList<>();

        switch (state) {
            case "ALL":
                bookingDtoList.addAll(bookingRepository.findAllByBooker(user, SORT));
                break;

            case "REJECTED":
                bookingDtoList.addAll(bookingRepository.findAllByBookerAndStatusEquals(user, BookingStatus.REJECTED, SORT));
                break;

            case "CURRENT":
                bookingDtoList.addAll(bookingRepository.findAllByBookerAndStartBeforeAndEndAfter(user,
                        LocalDateTime.now(), LocalDateTime.now(), SORT));
                break;

            case "PAST":
                bookingDtoList.addAll(bookingRepository.findAllByBookerAndEndBefore(user,
                        LocalDateTime.now(), SORT));
                break;

            case "WAITING":
                bookingDtoList.addAll(bookingRepository.findAllByBookerAndStatusEquals(user, BookingStatus.WAITING, SORT));
                break;

            case "FUTURE":
                bookingDtoList.addAll(bookingRepository.findAllByBookerAndStartAfter(user, LocalDateTime.now(), SORT));
                break;

            default:
                throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }

        return bookingDtoList.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getAllByOwnerId(Long ownerId, String state) {
        User user = userRepository.findById(ownerId).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь с id: %s не найден", ownerId)));

        List<Booking> bookingsList = new ArrayList<>();

        switch (state) {
            case "ALL":
                bookingsList.addAll(bookingRepository.findAllByItemOwner(user, SORT));
                break;

            case "FUTURE":
                bookingsList.addAll(bookingRepository.findAllByItemOwnerAndStartAfter(user, LocalDateTime.now(), SORT));
                break;

            case "CURRENT":
                bookingsList.addAll(bookingRepository.findAllByItemOwnerAndStartBeforeAndEndAfter(user,
                        LocalDateTime.now(), LocalDateTime.now(), SORT));
                break;

            case "PAST":
                bookingsList.addAll(bookingRepository.findAllByItemOwnerAndEndBefore(user,
                        LocalDateTime.now(), SORT));
                break;

            case "WAITING":
                bookingsList.addAll(bookingRepository.findAllByItemOwnerAndStatusEquals(user, BookingStatus.WAITING, SORT));
                break;

            case "REJECTED":
                bookingsList.addAll(bookingRepository.findAllByItemOwnerAndStatusEquals(user, BookingStatus.REJECTED, SORT));
                break;

            default:
                throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }

        return bookingsList.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public BookingDto getById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException(String.format("Некорректный id: %s брони", bookingId)));

        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException(String.format("Некорректный id: %s пользователя", userId));
        }

        return BookingMapper.toBookingDto(booking);
    }
}