package ru.skypro.homework.exception;

import java.io.IOException;

public class ImageNotUploadedException extends Throwable {
    public ImageNotUploadedException(String message) {
        super(message);
    }

    public ImageNotUploadedException(String message, Throwable cause) {
        super(message, cause);
    }
}
