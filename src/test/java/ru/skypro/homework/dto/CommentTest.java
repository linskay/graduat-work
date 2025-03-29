package ru.skypro.homework.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование DTO Comment")
public class CommentTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("Проверка корректного создания объекта Comment")
    void testValidComment() {
        Comment comment = Comment.builder()
                .pk(1)
                .text("Это пример комментария")
                .author(456L)
                .authorUsername("user123")
                .authorImage("https://example.com/avatar.jpg")
                .createdAt(1672502400000L)
                .build();

        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Проверка создания объекта Comment с пустым текстом")
    void testInvalidText_Blank() {
        Comment comment = Comment.builder()
                .pk(1)
                .text("")
                .author(456L)
                .authorUsername("user123")
                .authorImage("https://example.com/avatar.jpg")
                .createdAt(1672502400000L)
                .build();

        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Текст комментария не может быть пустым")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта Comment с текстом меньше минимальной длины")
    void testInvalidText_TooShort() {
        Comment comment = Comment.builder()
                .pk(1)
                .text("Hi")
                .author(456L)
                .authorUsername("user123")
                .authorImage("https://example.com/avatar.jpg")
                .createdAt(1672502400000L)
                .build();

        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Текст комментария должен содержать от 3 до 500 символов")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта Comment с пустым именем пользователя")
    void testInvalidAuthorUsername_Blank() {
        Comment comment = Comment.builder()
                .pk(1)
                .text("Это пример комментария")
                .author(456L)
                .authorUsername("")
                .authorImage("https://example.com/avatar.jpg")
                .createdAt(1672502400000L)
                .build();

        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Имя пользователя не может быть пустым")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта Comment с пустой ссылкой на аватар")
    void testInvalidAuthorImage_Blank() {
        Comment comment = Comment.builder()
                .pk(1)
                .text("Это пример комментария")
                .author(456L)
                .authorUsername("user123")
                .authorImage("")
                .createdAt(1672502400000L)
                .build();

        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Ссылка на аватар не может быть пустой")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта Comment с отрицательным временем создания")
    void testInvalidCreatedAt_Negative() {
        Comment comment = Comment.builder()
                .pk(1)
                .text("Это пример комментария")
                .author(456L)
                .authorUsername("user123")
                .authorImage("https://example.com/avatar.jpg")
                .createdAt(-1L)
                .build();

        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Время создания комментария должно быть положительным числом")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта Comment с пустым временем создания")
    void testInvalidCreatedAt_Null() {
        Comment comment = Comment.builder()
                .pk(1)
                .text("Это пример комментария")
                .author(456L)
                .authorUsername("user123")
                .authorImage("https://example.com/avatar.jpg")
                .createdAt(null)
                .build();

        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Время создания комментария не может быть пустым")))
                .isTrue();
    }
}