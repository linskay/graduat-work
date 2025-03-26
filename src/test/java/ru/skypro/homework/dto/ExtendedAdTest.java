package ru.skypro.homework.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование DTO ExtendedAd")
public class ExtendedAdTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("Проверка корректного создания объекта ExtendedAd")
    void testValidExtendedAd() {
        ExtendedAd ad = new ExtendedAd();
        ad.setAuthorFirstName("Олег");
        ad.setAuthorLastName("Олегович");
        ad.setDescription("Отличный товар, почти новый");
        ad.setEmail("ivan@example.com");
        ad.setPhone("+79991234567");

        ad.setImage("https://example.com/image.jpg");
        ad.setPk(1);
        ad.setAuthor(1L);
        ad.setPrice(1000);
        ad.setTitle("Продам велосипед");

        Set<ConstraintViolation<ExtendedAd>> violations = validator.validate(ad);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Проверка создания объекта ExtendedAd с пустым именем автора")
    void testInvalidAuthorFirstName_Blank() {
        ExtendedAd ad = new ExtendedAd();
        ad.setAuthorFirstName("");
        ad.setAuthorLastName("Олегович");
        ad.setDescription("Отличный товар, почти новый");
        ad.setEmail("ivan@example.com");
        ad.setPhone("+79991234567");

        Set<ConstraintViolation<ExtendedAd>> violations = validator.validate(ad);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Имя автора не может быть пустым")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта ExtendedAd с именем автора меньше минимальной длины")
    void testInvalidAuthorFirstName_TooShort() {
        ExtendedAd ad = new ExtendedAd();
        ad.setAuthorFirstName("A");
        ad.setAuthorLastName("Олегович");
        ad.setDescription("Отличный товар, почти новый");
        ad.setEmail("ivan@example.com");
        ad.setPhone("+79991234567");

        Set<ConstraintViolation<ExtendedAd>> violations = validator.validate(ad);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Имя автора должно содержать от 2 до 32 символов")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта ExtendedAd с пустой фамилией автора")
    void testInvalidAuthorLastName_Blank() {
        ExtendedAd ad = new ExtendedAd();
        ad.setAuthorFirstName("Олег");
        ad.setAuthorLastName("");
        ad.setDescription("Отличный товар, почти новый");
        ad.setEmail("ivan@example.com");
        ad.setPhone("+79991234567");

        Set<ConstraintViolation<ExtendedAd>> violations = validator.validate(ad);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Фамилия автора не может быть пустой")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта ExtendedAd с пустым описанием")
    void testInvalidDescription_Blank() {
        ExtendedAd ad = new ExtendedAd();
        ad.setAuthorFirstName("Олег");
        ad.setAuthorLastName("Олегович");
        ad.setDescription("");
        ad.setEmail("ivan@example.com");
        ad.setPhone("+79991234567");

        Set<ConstraintViolation<ExtendedAd>> violations = validator.validate(ad);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Описание не может быть пустым")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта ExtendedAd с некорректным email")
    void testInvalidEmail_InvalidFormat() {
        ExtendedAd ad = new ExtendedAd();
        ad.setAuthorFirstName("Олег");
        ad.setAuthorLastName("Олегович");
        ad.setDescription("Отличный товар, почти новый");
        ad.setEmail("invalid-email");
        ad.setPhone("+79991234567");

        Set<ConstraintViolation<ExtendedAd>> violations = validator.validate(ad);
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Некорректный формат email")))
                .isTrue();
    }

    @Test
    @DisplayName("Проверка создания объекта ExtendedAd с некорректным телефоном")
    void testInvalidPhone_InvalidFormat() {
        ExtendedAd ad = new ExtendedAd();
        ad.setAuthorFirstName("Олег");
        ad.setAuthorLastName("Олегович");
        ad.setDescription("Отличный товар, почти новый");
        ad.setEmail("ivan@example.com");
        ad.setPhone("12345");

        Set<ConstraintViolation<ExtendedAd>> violations = validator.validate(ad);
        assertThat(violations).isNotEmpty();
        boolean hasPhoneError = violations.stream()
                .anyMatch(v -> v.getMessage().contains("Неверный формат телефона"));
        assertThat(hasPhoneError).isTrue();
    }
}