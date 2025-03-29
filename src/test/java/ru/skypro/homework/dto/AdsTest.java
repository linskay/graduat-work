package ru.skypro.homework.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование DTO Ads")
public class AdsTest {

    @Test
    @DisplayName("Проверка корректного создания объекта Ads")
    void testValidAds() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            List<Ad> results = new ArrayList<>();
            results.add(new Ad());
            Ads ads = new Ads(results);

            Set<ConstraintViolation<Ads>> violations = validator.validate(ads);

            assertThat(violations).isEmpty();
        }
    }

    @Test
    @DisplayName("Проверка создания объекта Ads с пустым списком объявлений")
    void testEmptyResults() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            List<Ad> results = new ArrayList<>();
            Ads ads = new Ads(results);

            Set<ConstraintViolation<Ads>> violations = validator.validate(ads);

            assertThat(violations).isEmpty();
            assertThat(ads.getCount()).isEqualTo(0);
        }
    }

    @Test
    @DisplayName("Проверка создания объекта Ads с null в списке объявлений")
    void testNullResults() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Ads ads = new Ads(null);

            assertThat(ads.getCount()).isEqualTo(0);

            Set<ConstraintViolation<Ads>> violations = validator.validate(ads);

            assertThat(violations).isEmpty();
        }
    }

    @Test
    @DisplayName("Проверка создания объекта Ads с отрицательным значением count")
    void testInvalidCount_Negative() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            List<Ad> results = new ArrayList<>();
            Ads ads = new Ads(results);
            ads.setCount(-1);

            Set<ConstraintViolation<Ads>> violations = validator.validate(ads);

            assertThat(violations).isNotEmpty();
            assertThat(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("Количество объявлений не может быть отрицательным")))
                    .isTrue();
        }
    }

    @Test
    @DisplayName("Проверка создания объекта Ads с null в поле count")
    void testInvalidCount_Null() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();

            Ads ads = new Ads(new ArrayList<>());
            ads.setCount(null);

            Set<ConstraintViolation<Ads>> violations = validator.validate(ads);

            assertThat(violations).isNotEmpty();

            assertThat(violations.stream()
                    .anyMatch(v -> v.getMessage().contains("Количество объявлений не может быть пустым")))
                    .isTrue();
        }
    }
}