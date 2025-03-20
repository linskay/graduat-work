package ru.skypro.homework.exception;

import static ru.skypro.homework.exception.ErrorMessages.INVALID_PASSWORD;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message) {
        super(INVALID_PASSWORD);
    }
}
