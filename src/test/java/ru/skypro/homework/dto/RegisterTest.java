package ru.skypro.homework.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class RegisterTest {

    @Test
    @DisplayName("Проверка корректного создания объекта Register")
    void testValidRegister() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Register register = new Register();
            register.setUsername("ivan@example.com");
            register.setPassword("password123");
            register.setFirstName("Олег");
            register.setLastName("Олегов");
            register.setPhone("+7 (999) 123-45-67");
            register.setRole(Role.USER);

            Set<ConstraintViolation<Register>> violations = validator.validate(register);

            assertThat(violations).isEmpty();
        }
    }

    @Test
    @DisplayName("Проверка создания объекта Register с пустым логином")
    void testInvalidUsername_Blank() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Register register = new Register();
            register.setUsername("");
            register.setPassword("password123");
            register.setFirstName("Олег");
            register.setLastName("Олегов");
            register.setPhone("+7 (999) 123-45-67");
            register.setRole(Role.USER);

            Set<ConstraintViolation<Register>> violations = validator.validate(register);

            assertThat(violations).isNotEmpty();
            assertThat(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("Логин не может быть пустым")))
                    .isTrue();
        }
    }

    @Test
    @DisplayName("Проверка создания объекта Register с логином меньше 4 символов")
    void testInvalidUsername_TooShort() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Register register = new Register();
            register.setUsername("abc");
            register.setPassword("password123");
            register.setFirstName("Олег");
            register.setLastName("Олегов");
            register.setPhone("+7 (999) 123-45-67");
            register.setRole(Role.USER);

            Set<ConstraintViolation<Register>> violations = validator.validate(register);

            assertThat(violations).isNotEmpty();
            assertThat(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("Логин должен содержать от 4 до 32 символов")))
                    .isTrue();
        }
    }

    @Test
    @DisplayName("Проверка создания объекта Register с некорректным email")
    void testInvalidUsername_InvalidEmail() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Register register = new Register();
            register.setUsername("invalid-email");
            register.setPassword("password123");
            register.setFirstName("Олег");
            register.setLastName("Олегов");
            register.setPhone("+7 (999) 123-45-67");
            register.setRole(Role.USER);

            Set<ConstraintViolation<Register>> violations = validator.validate(register);

            assertThat(violations).isNotEmpty();
            assertThat(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("Email должен быть корректным адресом электронной почты")))
                    .isTrue();
        }
    }

    @Test
    @DisplayName("Проверка создания объекта Register с пустым паролем")
    void testInvalidPassword_Blank() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Register register = new Register();
            register.setUsername("ivan@example.com");
            register.setPassword(""); // Невалидное значение
            register.setFirstName("Олег");
            register.setLastName("Олегов");
            register.setPhone("+7 (999) 123-45-67");
            register.setRole(Role.USER);

            Set<ConstraintViolation<Register>> violations = validator.validate(register);

            assertThat(violations).isNotEmpty();
            assertThat(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("Пароль не может быть пустым")))
                    .isTrue();
        }
    }

    @Test
    @DisplayName("Проверка создания объекта Register с паролем меньше 8 символов")
    void testInvalidPassword_TooShort() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Register register = new Register();
            register.setUsername("ivan@example.com");
            register.setPassword("short");
            register.setFirstName("Олег");
            register.setLastName("Олегов");
            register.setPhone("+7 (999) 123-45-67");
            register.setRole(Role.USER);

            Set<ConstraintViolation<Register>> violations = validator.validate(register);

            assertThat(violations).isNotEmpty();
            assertThat(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("Пароль должен содержать от 8 до 16 символов")))
                    .isTrue();
        }
    }

    @Test
    @DisplayName("Проверка создания объекта Register с пустой фамилией")
    void testInvalidLastName_Blank() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Register register = new Register();
            register.setUsername("ivan@example.com");
            register.setPassword("password123");
            register.setFirstName("Олег");
            register.setLastName("");
            register.setPhone("+7 (999) 123-45-67");
            register.setRole(Role.USER);

            Set<ConstraintViolation<Register>> violations = validator.validate(register);

            assertThat(violations).isNotEmpty();
            assertThat(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("Фамилия не может быть пустой")))
                    .isTrue();
        }
    }

    @Test
    @DisplayName("Проверка создания объекта Register с телефоном неверного формата")
    void testInvalidPhone_InvalidFormat() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Register register = new Register();
            register.setUsername("ivan@example.com");
            register.setPassword("password123");
            register.setFirstName("Олег");
            register.setLastName("Олегов");
            register.setPhone("invalid-phone");
            register.setRole(Role.USER);

            Set<ConstraintViolation<Register>> violations = validator.validate(register);

            assertThat(violations).isNotEmpty();
            assertThat(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("Неверный формат телефона")))
                    .isTrue();
        }
    }

    @Test
    @DisplayName("Проверка создания объекта Register с пустой ролью")
    void testInvalidRole_Null() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Register register = new Register();
            register.setUsername("ivan@example.com");
            register.setPassword("password123");
            register.setFirstName("Олег");
            register.setLastName("Олегов");
            register.setPhone("+7 (999) 123-45-67");
            register.setRole(null);

            Set<ConstraintViolation<Register>> violations = validator.validate(register);

            assertThat(violations).isNotEmpty();
            assertThat(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("Роль не может быть пустой")))
                    .isTrue();
        }
    }
}