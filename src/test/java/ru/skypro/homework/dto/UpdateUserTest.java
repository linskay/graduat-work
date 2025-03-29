package ru.skypro.homework.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование DTO UpdateUser")
public class UpdateUserTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("Проверка корректного создания объекта UpdateUser")
    void testValidUpdateUser() {
        UpdateUser user = new UpdateUser();
        user.setFirstName("Олег");
        user.setLastName("Олегович");
        user.setPhone("+79991234567");

        Set<ConstraintViolation<UpdateUser>> violations = validator.validate(user);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Проверка создания объекта UpdateUser с firstName равным null")
    void testInvalidFirstName_Null() {
        UpdateUser user = new UpdateUser();
        user.setFirstName(null);
        user.setLastName("Олегович");
        user.setPhone("+79991234567");

        Set<ConstraintViolation<UpdateUser>> violations = validator.validate(user);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("firstName не может быть null")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта UpdateUser с firstName меньше минимальной длины")
    void testInvalidFirstName_TooShort() {
        UpdateUser user = new UpdateUser();
        user.setFirstName("Ол");
        user.setLastName("Олегович");
        user.setPhone("+79991234567");

        Set<ConstraintViolation<UpdateUser>> violations = validator.validate(user);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Имя должно содержать от 3 до 10 символов")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта UpdateUser с firstName больше максимальной длины")
    void testInvalidFirstName_TooLong() {
        UpdateUser user = new UpdateUser();
        user.setFirstName("ОченьДлинноеИмя");
        user.setLastName("Олегович");
        user.setPhone("+79991234567");

        Set<ConstraintViolation<UpdateUser>> violations = validator.validate(user);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Имя должно содержать от 3 до 10 символов")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта UpdateUser с lastName равным null")
    void testInvalidLastName_Null() {
        UpdateUser user = new UpdateUser();
        user.setFirstName("Олег");
        user.setLastName(null);
        user.setPhone("+79991234567");

        Set<ConstraintViolation<UpdateUser>> violations = validator.validate(user);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("lastName не может быть null")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта UpdateUser с lastName меньше минимальной длины")
    void testInvalidLastName_TooShort() {
        UpdateUser user = new UpdateUser();
        user.setFirstName("Олег");
        user.setLastName("Ол");
        user.setPhone("+79991234567");

        Set<ConstraintViolation<UpdateUser>> violations = validator.validate(user);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Фамилия должна содержать от 3 до 10 символов")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта UpdateUser с lastName больше максимальной длины")
    void testInvalidLastName_TooLong() {
        UpdateUser user = new UpdateUser();
        user.setFirstName("Олег");
        user.setLastName("ОченьДлиннаяФамилия");
        user.setPhone("+79991234567");

        Set<ConstraintViolation<UpdateUser>> violations = validator.validate(user);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Фамилия должна содержать от 3 до 10 символов")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта UpdateUser с пустым телефоном")
    void testInvalidPhone_Blank() {
        UpdateUser user = new UpdateUser();
        user.setFirstName("Олег");
        user.setLastName("Олегович");
        user.setPhone("");

        Set<ConstraintViolation<UpdateUser>> violations = validator.validate(user);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Телефон не может быть пустым")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта UpdateUser с телефоном неверного формата")
    void testInvalidPhone_InvalidFormat() {
        UpdateUser user = new UpdateUser();
        user.setFirstName("Олег");
        user.setLastName("Олегович");
        user.setPhone("12345");

        Set<ConstraintViolation<UpdateUser>> violations = validator.validate(user);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Неверный формат телефона")))
                .isTrue();
    }
}