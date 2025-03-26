package ru.skypro.homework.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование DTO NewPassword")
public class NewPasswordTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("Проверка корректного создания объекта NewPassword")
    void testValidNewPassword() {
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("oldPassword123");
        newPassword.setNewPassword("newPassword123");

        Set<ConstraintViolation<NewPassword>> violations = validator.validate(newPassword);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Проверка создания объекта NewPassword с пустым текущим паролем")
    void testInvalidCurrentPassword_Blank() {
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("");
        newPassword.setNewPassword("newPassword123");

        Set<ConstraintViolation<NewPassword>> violations = validator.validate(newPassword);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Текущий пароль не может быть пустым")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта NewPassword с текущим паролем меньше минимальной длины")
    void testInvalidCurrentPassword_TooShort() {
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("short");
        newPassword.setNewPassword("newPassword123");

        Set<ConstraintViolation<NewPassword>> violations = validator.validate(newPassword);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Текущий пароль должен содержать от 8 до 16 символов")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта NewPassword с текущим паролем больше максимальной длины")
    void testInvalidCurrentPassword_TooLong() {
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("A".repeat(17));
        newPassword.setNewPassword("newPassword123");

        Set<ConstraintViolation<NewPassword>> violations = validator.validate(newPassword);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Текущий пароль должен содержать от 8 до 16 символов")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта NewPassword с пустым новым паролем")
    void testInvalidNewPassword_Blank() {
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("oldPassword123");
        newPassword.setNewPassword("");

        Set<ConstraintViolation<NewPassword>> violations = validator.validate(newPassword);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Новый пароль не может быть пустым")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта NewPassword с новым паролем меньше минимальной длины")
    void testInvalidNewPassword_TooShort() {
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("oldPassword123");
        newPassword.setNewPassword("short");

        Set<ConstraintViolation<NewPassword>> violations = validator.validate(newPassword);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Новый пароль должен содержать от 8 до 16 символов")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта NewPassword с новым паролем больше максимальной длины")
    void testInvalidNewPassword_TooLong() {
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("oldPassword123");
        newPassword.setNewPassword("A".repeat(17));

        Set<ConstraintViolation<NewPassword>> violations = validator.validate(newPassword);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Новый пароль должен содержать от 8 до 16 символов")))
                .isTrue();
    }
}