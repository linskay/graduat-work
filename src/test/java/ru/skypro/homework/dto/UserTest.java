package ru.skypro.homework.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование DTO User")
public class UserTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("Проверка корректного создания объекта User")
    void testValidUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Иван");
        user.setLastName("Иванов");
        user.setEmail("ivan@example.com");
        user.setPhone("+79991234567");
        user.setImage("https://example.com/image.jpg");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Проверка создания объекта User с некорректным email")
    void testInvalidEmail_InvalidFormat() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Иван");
        user.setLastName("Иванов");
        user.setEmail("invalid-email");
        user.setPhone("+79991234567");
        user.setImage("https://example.com/image.jpg");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Email должен быть корректным адресом электронной почты")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта User с пустым телефоном")
    void testInvalidPhone_Blank() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Иван");
        user.setLastName("Иванов");
        user.setEmail("ivan@example.com");
        user.setPhone("");
        user.setImage("https://example.com/image.jpg");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Телефон не может быть пустым")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта User с телефоном неверного формата")
    void testInvalidPhone_InvalidFormat() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Иван");
        user.setLastName("Иванов");
        user.setEmail("ivan@example.com");
        user.setPhone("12345");
        user.setImage("https://example.com/image.jpg");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Неверный формат телефона")))
                .isTrue();

    }
}