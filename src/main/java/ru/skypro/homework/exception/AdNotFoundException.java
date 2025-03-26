package ru.skypro.homework.exception;

public class AdNotFoundException extends RuntimeException {
    public AdNotFoundException(Integer id) {
        super("Ad not found with id: " + id);
    }
}