package ru.skypro.homework.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование DTO CreateOrUpdateComment")
public class CreateOrUpdateCommentTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("Проверка корректного создания объекта CreateOrUpdateComment")
    void testValidCreateOrUpdateComment() {
        CreateOrUpdateComment comment = new CreateOrUpdateComment();
        comment.setText("Это пример текста комментария");

        Set<ConstraintViolation<CreateOrUpdateComment>> violations = validator.validate(comment);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Проверка создания объекта CreateOrUpdateComment с пустым текстом")
    void testInvalidText_Blank() {
        CreateOrUpdateComment comment = new CreateOrUpdateComment();
        comment.setText("");

        Set<ConstraintViolation<CreateOrUpdateComment>> violations = validator.validate(comment);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Текст комментария не может быть пустым")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта CreateOrUpdateComment с текстом меньше минимальной длины")
    void testInvalidText_TooShort() {
        CreateOrUpdateComment comment = new CreateOrUpdateComment();
        comment.setText("Short");

        Set<ConstraintViolation<CreateOrUpdateComment>> violations = validator.validate(comment);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Текст комментария должен содержать от 8 до 64 символов")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта CreateOrUpdateComment с текстом больше максимальной длины")
    void testInvalidText_TooLong() {
        CreateOrUpdateComment comment = new CreateOrUpdateComment();
        comment.setText("A".repeat(65));

        Set<ConstraintViolation<CreateOrUpdateComment>> violations = validator.validate(comment);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Текст комментария должен содержать от 8 до 64 символов")))
                .isTrue();
    }
}