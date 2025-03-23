package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;

import javax.validation.Valid;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Tag(name = "Аутентификация", description = "Операции для регистрации и авторизации пользователей")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(
            summary = "Авторизация пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
            }
    )
    public ResponseEntity<Void> login(@RequestBody @Valid Login login) {
        log.info("Запрос на авторизацию пользователя: {}", login.getUsername());

        boolean isAuthenticated = authService.login(login.getUsername(), login.getPassword());
        if (isAuthenticated) {
            log.info("Пользователь успешно авторизован: {}", login.getUsername());
            return ResponseEntity.ok().build();
        } else {
            log.warn("Неудачная попытка авторизации для пользователя: {}", login.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    @Operation(
            summary = "Регистрация пользователя",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created"),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content()),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content())
            }
    )
    public ResponseEntity<Long> register(@RequestBody @Valid Register register) {
        log.info("Запрос на регистрацию пользователя: {}", register.getUsername());

        try {
            Long userId = authService.register(register);
            log.info("Пользователь успешно зарегистрирован: {}", register.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(userId);
        } catch (Exception e) {
            log.error("Ошибка при регистрации пользователя: {}", register.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}