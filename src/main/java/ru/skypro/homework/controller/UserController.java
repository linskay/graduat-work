package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.service.UserService;

import javax.validation.Valid;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи", description = "Эндпоинты для работы с пользователями")
public class UserController {

    private final UserService userService;
    private UserMapper userMapper;

    @PostMapping("/set_password")
    @Operation(
            summary = "Обновление пароля",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content()),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
            }
    )
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> setPassword(@Valid @RequestBody NewPassword newPassword) {
        log.info("Запрос на смену пароля");
        userService.changePassword(newPassword);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    @Operation(
            summary = "Получение информации об авторизованном пользователе",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Информация о пользователе", content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = User.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "Неавторизованный доступ", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content()
                    )
            }
    )
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> getUser() {
        log.info("Запрос на получение информации о пользователе");
        return ResponseEntity.ok(userService.getUser());
    }

    @PatchMapping("/me")
    @Operation(
            summary = "Обновление информации об авторизованном пользователе",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UpdateUser.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UpdateUser> updateUser(@RequestBody UpdateUser updateUser) {
        log.info("Начало обновления информации о пользователе");

        UpdateUser updatedUser = userService.updateUser(updateUser);

        log.info("Информация о пользователе успешно обновлена");
        return ResponseEntity.ok(updateUser);
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Обновление аватара авторизованного пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> updateUserImage(@RequestParam("image") MultipartFile image) {
        userService.updateUserAvatar(image);
        return ResponseEntity.ok().build();
    }
}