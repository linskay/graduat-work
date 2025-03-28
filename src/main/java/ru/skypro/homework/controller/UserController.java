package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.util.ImageUtils;

import javax.validation.Valid;
import java.io.IOException;

@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи", description = "Эндпоинты для работы с пользователями")
public class UserController {

    private final UserService userService;

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
        return ResponseEntity.ok(userService.getUser());
    }

    @PatchMapping("/me")
    @Operation(
            summary = "Обновление информации об авторизованном пользователе",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UpdateUser.class))),
                    @ApiResponse(responseCode = "401", description = "Не авторизован"),
                    @ApiResponse(responseCode = "403", description = "Запрещено"),
                    @ApiResponse(responseCode = "404", description = "Не найдено")
            }
    )
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UpdateUser> updateUser(@Valid @RequestBody UpdateUser updateUser) {
        UpdateUser updatedUser = userService.updateUser(updateUser);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Обновление аватара авторизованного пользователя")
    public ResponseEntity<String> updateUserImage(@RequestParam("image") MultipartFile file) throws IOException {

        String newFileName = userService.updateUserImage(file);
        return ResponseEntity.ok(newFileName);
    }
}
