package ru.skypro.homework.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование DTO Login")
public class LoginTest {

    @Test
    @DisplayName("Проверка корректного создания объекта Login")
    void testValidLogin() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Login login = new Login();
            login.setUsername("user@example.com");
            login.setPassword("password123");

            Set<ConstraintViolation<Login>> violations = validator.validate(login);

            assertThat(violations).isEmpty();
        }
    }

    @Test
    @DisplayName("Проверка создания объекта Login с пустым логином")
    void testInvalidUsername_Blank() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Login login = new Login();
            login.setUsername("");
            login.setPassword("password123");

            Set<ConstraintViolation<Login>> violations = validator.validate(login);

            assertThat(violations).isNotEmpty();
            assertThat(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("Логин не может быть пустым")))
                    .isTrue();
        }
    }

    @Test
    @DisplayName("Проверка создания объекта Login с логином меньше 4 символов")
    void testInvalidUsername_TooShort() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Login login = new Login();
            login.setUsername("abc");
            login.setPassword("password123");

            Set<ConstraintViolation<Login>> violations = validator.validate(login);

            assertThat(violations).isNotEmpty();
            assertThat(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("Логин должен содержать от 4 до 32 символов")))
                    .isTrue();
        }
    }

    @Test
    @DisplayName("Проверка создания объекта Login с логином больше 32 символов")
    void testInvalidUsername_TooLong() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Login login = new Login();
            login.setUsername("a".repeat(33));
            login.setPassword("password123");

            Set<ConstraintViolation<Login>> violations = validator.validate(login);
            assertThat(violations).isNotEmpty();
            assertThat(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("Логин должен содержать от 4 до 32 символов")))
                    .isTrue();
        }
    }

    @Test
    @DisplayName("Проверка создания объекта Login с некорректным email")
    void testInvalidUsername_InvalidEmail() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Login login = new Login();
            login.setUsername("invalid-email");
            login.setPassword("password123");

            Set<ConstraintViolation<Login>> violations = validator.validate(login);

            assertThat(violations).isNotEmpty();
            assertThat(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("Email должен быть корректным адресом электронной почты")))
                    .isTrue();
        }
    }

    @Test
    @DisplayName("Проверка создания объекта Login с пустым паролем")
    void testInvalidPassword_Blank() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Login login = new Login();
            login.setUsername("user@example.com");
            login.setPassword("");

            Set<ConstraintViolation<Login>> violations = validator.validate(login);

            assertThat(violations).isNotEmpty();
            assertThat(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("Пароль не может быть пустым")))
                    .isTrue();
        }
    }

    @Test
    @DisplayName("Проверка создания объекта Login с паролем меньше 8 символов")
    void testInvalidPassword_TooShort() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Login login = new Login();
            login.setUsername("user@example.com");
            login.setPassword("short");

            Set<ConstraintViolation<Login>> violations = validator.validate(login);

            assertThat(violations).isNotEmpty();
            assertThat(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("Пароль должен содержать от 8 до 16 символов")))
                    .isTrue();
        }
    }

    @Test
    @DisplayName("Проверка создания объекта Login с паролем больше 16 символов")
    void testInvalidPassword_TooLong() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Login login = new Login();
            login.setUsername("user@example.com");
            login.setPassword("passionflower123456789");

            Set<ConstraintViolation<Login>> violations = validator.validate(login);

            assertThat(violations).isNotEmpty();
            assertThat(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("Пароль должен содержать от 8 до 16 символов")))
                    .isTrue();
        }
    }
}