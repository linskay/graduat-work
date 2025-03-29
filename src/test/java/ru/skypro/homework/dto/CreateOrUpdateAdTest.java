package ru.skypro.homework.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование DTO CreateOrUpdateAd")
public class CreateOrUpdateAdTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("Проверка корректного создания объекта CreateOrUpdateAd")
    void testValidCreateOrUpdateAd() {
        CreateOrUpdateAd ad = new CreateOrUpdateAd();
        ad.setTitle("Продам велосипед");
        ad.setPrice(1000);
        ad.setDescription("Отличный велосипед, почти новый");

        Set<ConstraintViolation<CreateOrUpdateAd>> violations = validator.validate(ad);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Проверка создания объекта CreateOrUpdateAd с пустым заголовком")
    void testInvalidTitle_Blank() {
        CreateOrUpdateAd ad = new CreateOrUpdateAd();
        ad.setTitle("");
        ad.setPrice(1000);
        ad.setDescription("Отличный велосипед, почти новый");

        Set<ConstraintViolation<CreateOrUpdateAd>> violations = validator.validate(ad);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Заголовок не может быть пустым")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта CreateOrUpdateAd с заголовком меньше минимальной длины")
    void testInvalidTitle_TooShort() {
        CreateOrUpdateAd ad = new CreateOrUpdateAd();
        ad.setTitle("ABC");
        ad.setPrice(1000);
        ad.setDescription("Отличный велосипед, почти новый");

        Set<ConstraintViolation<CreateOrUpdateAd>> violations = validator.validate(ad);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Заголовок должен содержать от 4 до 32 символов")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта CreateOrUpdateAd с заголовком больше максимальной длины")
    void testInvalidTitle_TooLong() {
        CreateOrUpdateAd ad = new CreateOrUpdateAd();
        ad.setTitle("A".repeat(33));
        ad.setPrice(1000);
        ad.setDescription("Отличный велосипед, почти новый");

        Set<ConstraintViolation<CreateOrUpdateAd>> violations = validator.validate(ad);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Заголовок должен содержать от 4 до 32 символов")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта CreateOrUpdateAd с пустой ценой")
    void testInvalidPrice_Null() {
        CreateOrUpdateAd ad = new CreateOrUpdateAd();
        ad.setTitle("Продам велосипед");
        ad.setPrice(null);
        ad.setDescription("Отличный велосипед, почти новый");

        Set<ConstraintViolation<CreateOrUpdateAd>> violations = validator.validate(ad);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Цена не может быть пустой")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта CreateOrUpdateAd с отрицательной ценой")
    void testInvalidPrice_Negative() {
        CreateOrUpdateAd ad = new CreateOrUpdateAd();
        ad.setTitle("Продам велосипед");
        ad.setPrice(-100);
        ad.setDescription("Отличный велосипед, почти новый");

        Set<ConstraintViolation<CreateOrUpdateAd>> violations = validator.validate(ad);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Цена должна быть не меньше 0")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта CreateOrUpdateAd с ценой больше максимальной")
    void testInvalidPrice_TooHigh() {
        CreateOrUpdateAd ad = new CreateOrUpdateAd();
        ad.setTitle("Продам велосипед");
        ad.setPrice(15_000_000);
        ad.setDescription("Отличный велосипед, почти новый");

        Set<ConstraintViolation<CreateOrUpdateAd>> violations = validator.validate(ad);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Цена должна быть не больше 10,000,000")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта CreateOrUpdateAd с пустым описанием")
    void testInvalidDescription_Blank() {
        CreateOrUpdateAd ad = new CreateOrUpdateAd();
        ad.setTitle("Продам велосипед");
        ad.setPrice(1000);
        ad.setDescription("");

        Set<ConstraintViolation<CreateOrUpdateAd>> violations = validator.validate(ad);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Описание не может быть пустым")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта CreateOrUpdateAd с описанием меньше минимальной длины")
    void testInvalidDescription_TooShort() {
        CreateOrUpdateAd ad = new CreateOrUpdateAd();
        ad.setTitle("Продам велосипед");
        ad.setPrice(1000);
        ad.setDescription("Short");

        Set<ConstraintViolation<CreateOrUpdateAd>> violations = validator.validate(ad);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Описание должно содержать от 8 до 64 символов")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта CreateOrUpdateAd с описанием больше максимальной длины")
    void testInvalidDescription_TooLong() {
        CreateOrUpdateAd ad = new CreateOrUpdateAd();
        ad.setTitle("Продам велосипед");
        ad.setPrice(1000);
        ad.setDescription("A".repeat(65));

        Set<ConstraintViolation<CreateOrUpdateAd>> violations = validator.validate(ad);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Описание должно содержать от 8 до 64 символов")))
                .isTrue();
    }
}