package ru.skypro.homework.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование DTO Comments")
public class CommentsTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("Проверка корректного создания объекта Comments")
    void testValidComments() {
        Comments comments = Comments.builder()
                .count(5)
                .results(List.of(
                        Comment.builder()
                                .pk(1)
                                .text("Это пример комментария")
                                .author(456L)
                                .authorUsername("user123")
                                .authorImage("https://example.com/avatar.jpg")
                                .createdAt(1672502400000L)
                                .build()
                ))
                .build();

        Set<ConstraintViolation<Comments>> violations = validator.validate(comments);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Проверка создания объекта Comments с пустым количеством комментариев")
    void testInvalidCount_Null() {
        Comments comments = Comments.builder()
                .count(null)
                .results(List.of(
                        Comment.builder()
                                .pk(1)
                                .text("Это пример комментария")
                                .author(456L)
                                .authorUsername("user123")
                                .authorImage("https://example.com/avatar.jpg")
                                .createdAt(1672502400000L)
                                .build()
                ))
                .build();

        Set<ConstraintViolation<Comments>> violations = validator.validate(comments);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Количество комментариев не может быть пустым")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта Comments с отрицательным количеством комментариев")
    void testInvalidCount_Negative() {
        Comments comments = Comments.builder()
                .count(-1) // Отрицательное значение
                .results(List.of(
                        Comment.builder()
                                .pk(1)
                                .text("Это пример комментария")
                                .author(456L)
                                .authorUsername("user123")
                                .authorImage("https://example.com/avatar.jpg")
                                .createdAt(1672502400000L)
                                .build()
                ))
                .build();

        Set<ConstraintViolation<Comments>> violations = validator.validate(comments);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Количество комментариев должно быть неотрицательным числом")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта Comments с пустым списком комментариев")
    void testInvalidResults_Null() {
        Comments comments = Comments.builder()
                .count(5)
                .results(null)
                .build();

        Set<ConstraintViolation<Comments>> violations = validator.validate(comments);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Список комментариев не может быть пустым")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта Comments с пустым списком комментариев (пустой список)")
    void testInvalidResults_EmptyList() {
        Comments comments = Comments.builder()
                .count(5)
                .results(List.of())
                .build();

        Set<ConstraintViolation<Comments>> violations = validator.validate(comments);
        assertThat(violations).isEmpty();
    }
}