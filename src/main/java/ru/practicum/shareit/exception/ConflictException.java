package ru.practicum.shareit.exception;

public class ConflictException extends IllegalArgumentException {
    public ConflictException(String m) {
        super(m);
    }
}
