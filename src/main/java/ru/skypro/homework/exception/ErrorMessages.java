package ru.skypro.homework.exception;

public final class ErrorMessages {
    public static final String AD_NOT_FOUND = "Объявление не найдено";
    public static final String COMMENT_NOT_FOUND = "Комментарий не найден";
    public static final String COMMENT_NOT_BELONG_TO_AD = "Комментарий не относится к указанному объявлению";
    public static final String UNAUTHORIZED_ACCESS = "У вас нет прав для выполнения этого действия";

    public static final String IMAGE_UPLOAD_FAILED = "Ошибка при загрузке изображения";
    public static final String IMAGE_FILE_EMPTY = "Файл изображения пуст";

    public static final String USER_NOT_AUTHORIZED = "Пользователь не авторизован";
    public static final String USER_NOT_FOUND = "Пользователь не найден";
    public static final String INVALID_PASSWORD = "Текущий пароль неверный";

    private ErrorMessages() {
    }
}