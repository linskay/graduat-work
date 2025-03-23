package ru.skypro.homework.exception;

public final class ErrorMessages {
    public static final String AD_NOT_FOUND = "Объявление %s не найдено";
    public static final String COMMENT_NOT_FOUND = "Комментарий не найден";
    public static final String COMMENT_NOT_BELONG_TO_AD = "Комментарий не относится к указанному объявлению";
    public static final String UNAUTHORIZED_ACCESS = "У вас нет прав для выполнения этого действия";

    public static final String IMAGE_UPLOAD_FAILED = "Ошибка при загрузке изображения";
    public static final String IMAGE_FILE_EMPTY = "Файл изображения пуст";

    public static final String USER_NOT_AUTHORIZED = "Пользователь не авторизован";
    public static final String USER_NOT_FOUND = "Пользователь с email %s не найден";
    public static final String INVALID_PASSWORD = "Текущий пароль неверный";
    public static final String IMAGE_FILE_INVALID_FORMAT = "Неверный формат изображения";
    public static final String INVALID_CURRENT_PASSWORD = "Некорректный старый пароль";
    public static final String INVALID_CREDENTIALS = "Неверные учетные данные";
    public static final String USER_ALREADY_EXISTS = "Пользователь с email %s уже существует";

    private ErrorMessages() {
    }
}