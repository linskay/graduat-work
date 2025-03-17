package ru.skypro.homework.exception;

public class CommentNotBelongToAdException extends RuntimeException {
    public CommentNotBelongToAdException(String message) {
        super(message);
    }
}