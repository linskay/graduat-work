package ru.skypro.homework.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование DTO Ad")
public class AdTest {

    @Test
    @DisplayName("Проверка корректного создания объекта Ad")
    void testValidAd() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Ad ad = new Ad();
            ad.setAuthor(1L);
            ad.setImage("https://example.com/image.jpg");
            ad.setPk(456);
            ad.setPrice(1000);
            ad.setTitle("Продам велосипед");

            Set<ConstraintViolation<Ad>> violations = validator.validate(ad);

            assertThat(violations).isEmpty();
        }
    }

    @Test
    @DisplayName("Проверка создания объекта Ad с пустым идентификатором автора")
    void testInvalidAuthor_Null() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Ad ad = new Ad();
            ad.setAuthor(null);
            ad.setImage("https://example.com/image.jpg");
            ad.setPk(456);
            ad.setPrice(1000);
            ad.setTitle("Продам велосипед");

            Set<ConstraintViolation<Ad>> violations = validator.validate(ad);

            assertThat(violations).isNotEmpty();
            assertThat(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("Идентификатор автора не может быть пустым")))
                    .isTrue();
        }
    }

    @Test
    @DisplayName("Проверка создания объекта Ad с пустой ссылкой на изображение")
    void testInvalidImage_Blank() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Ad ad = new Ad();
            ad.setAuthor(1L);
            ad.setImage("");
            ad.setPk(456);
            ad.setPrice(1000);
            ad.setTitle("Продам велосипед");

            Set<ConstraintViolation<Ad>> violations = validator.validate(ad);

            assertThat(violations).isNotEmpty();
            assertThat(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("Ссылка на изображение не может быть пустой")))
                    .isTrue();
        }
    }

    @Test
    @DisplayName("Проверка создания объекта Ad с пустым уникальным идентификатором")
    void testInvalidPk_Null() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Ad ad = new Ad();
            ad.setAuthor(1L);
            ad.setImage("https://example.com/image.jpg");
            ad.setPk(null);
            ad.setPrice(1000);
            ad.setTitle("Продам велосипед");

            Set<ConstraintViolation<Ad>> violations = validator.validate(ad);

            assertThat(violations).isNotEmpty();
            assertThat(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("Уникальный идентификатор объявления не может быть пустым")))
                    .isTrue();
        }
    }

    @Test
    @DisplayName("Проверка создания объекта Ad с пустой ценой")
    void testInvalidPrice_Null() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Ad ad = new Ad();
            ad.setAuthor(1L);
            ad.setImage("https://example.com/image.jpg");
            ad.setPk(456);
            ad.setPrice(null);
            ad.setTitle("Продам велосипед");

            Set<ConstraintViolation<Ad>> violations = validator.validate(ad);

            assertThat(violations).isNotEmpty();
            assertThat(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("Цена не может быть пустой")))
                    .isTrue();
        }
    }

    @Test
    @DisplayName("Проверка создания объекта Ad с отрицательной ценой")
    void testInvalidPrice_Negative() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Ad ad = new Ad();
            ad.setAuthor(1L);
            ad.setImage("https://example.com/image.jpg");
            ad.setPk(456);
            ad.setPrice(-100);
            ad.setTitle("Продам велосипед");

            Set<ConstraintViolation<Ad>> violations = validator.validate(ad);

            assertThat(violations).isNotEmpty();
            assertThat(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("Цена должна быть больше или равна 0")))
                    .isTrue();
        }
    }

    @Test
    @DisplayName("Проверка создания объекта Ad с ценой, превышающей максимально допустимое значение")
    void testInvalidPrice_TooHigh() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Ad ad = new Ad();
            ad.setAuthor(1L);
            ad.setImage("https://example.com/image.jpg");
            ad.setPk(456);
            ad.setPrice(10000001);
            ad.setTitle("Продам велосипед");

            Set<ConstraintViolation<Ad>> violations = validator.validate(ad);

            assertThat(violations).isNotEmpty();
            assertThat(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("Цена должна быть меньше или равна 10,000,000")))
                    .isTrue();
        }
    }

    @Test
    @DisplayName("Проверка создания объекта Ad с пустым заголовком")
    void testInvalidTitle_Blank() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Ad ad = new Ad();
            ad.setAuthor(1L);
            ad.setImage("https://example.com/image.jpg");
            ad.setPk(456);
            ad.setPrice(1000);
            ad.setTitle("");

            Set<ConstraintViolation<Ad>> violations = validator.validate(ad);

            assertThat(violations).isNotEmpty();
            assertThat(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("Заголовок не может быть пустым")))
                    .isTrue();
        }
    }

    @Test
    @DisplayName("Проверка создания объекта Ad с заголовком меньше 4 символов")
    void testInvalidTitle_TooShort() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Ad ad = new Ad();
            ad.setAuthor(1L);
            ad.setImage("https://example.com/image.jpg");
            ad.setPk(456);
            ad.setPrice(1000);
            ad.setTitle("abc");

            Set<ConstraintViolation<Ad>> violations = validator.validate(ad);

            assertThat(violations).isNotEmpty();
            assertThat(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("Заголовок должен содержать от 4 до 32 символов")))
                    .isTrue();
        }
    }

    @Test
    @DisplayName("Проверка создания объекта Ad с заголовком больше 32 символов")
    void testInvalidTitle_TooLong() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Ad ad = new Ad();
            ad.setAuthor(1L);
            ad.setImage("https://example.com/image.jpg");
            ad.setPk(456);
            ad.setPrice(1000);
            ad.setTitle("a".repeat(33));

            Set<ConstraintViolation<Ad>> violations = validator.validate(ad);

            assertThat(violations).isNotEmpty();
            assertThat(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("Заголовок должен содержать от 4 до 32 символов")))
                    .isTrue();
        }
    }
}