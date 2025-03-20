package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи", description = "Операции с пользователями")
public class UserController {

    private final UserService userService;

    @PostMapping("/set_password") //toDo тесты - метод работает через раз: 500/200
    @Operation(
            summary = "Обновление пароля",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @ResponseStatus(HttpStatus.OK)
    public void setPassword(@Valid @RequestBody NewPassword newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        log.debug("Получен объект NewPassword: {}", newPassword);
        log.debug("currentPassword: {}", newPassword.getCurrentPassword());
        log.debug("newPassword: {}", newPassword.getNewPassword());

        log.info("Запрос на смену пароля для пользователя: {}", email);
        userService.changePassword(email, newPassword);
        log.info("Пароль успешно изменен для пользователя: {}", email);
    }

    @GetMapping("/me")
    @Operation(
            summary = "Получение информации об авторизованном пользователе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация о пользователе",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = User.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Неавторизованный доступ"
                    )
            }
    )
    public ru.skypro.homework.model.User getUser() {
        return userService.getCurrentUser();
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Обновление аватара авторизованного пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Аватар обновлен"),
                    @ApiResponse(responseCode = "401", description = "Неавторизованный доступ")
            }
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public String updateUserImage(@RequestParam("image") MultipartFile image) throws IOException {
        userService.updateUserImage(image);
        return "Аватар успешно обновлен";
    }


    @PatchMapping("/me")
    @Operation(
            summary = "Обновление информации об авторизованном пользователе",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content
                            (schema = @Schema(implementation = UpdateUser.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> updateUser(@Valid @RequestBody UpdateUser updateUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        userService.updateUser(email, updateUser);

        log.info("Информация о пользователе успешно обновлена: {}", email);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Данные пользователя успешно обновлены");
        return response;
    }
}