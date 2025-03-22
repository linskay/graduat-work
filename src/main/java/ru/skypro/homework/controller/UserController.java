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
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.service.FileService;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.mapper.UserMapper;

import javax.validation.Valid;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи", description = "Операции с пользователями")
public class UserController {

    private final UserService userService;
    private final FileService fileService;
    private UserMapper userMapper;

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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> getUser() {
        log.info("Начало получения информации о пользователе");

        User user = userService.getUser();

        log.info("Информация о пользователе успешно получена");
        return ResponseEntity.ok(user);
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) //toDo на фронте не отображается
    @Operation(
            summary = "Обновление аватара авторизованного пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Аватар обновлен"),
                    @ApiResponse(responseCode = "401", description = "Неавторизованный доступ")
            }
    )
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> updateUserImage(@RequestPart("image") MultipartFile image) {
        try {
            userService.saveImage(image);
            
            UserEntity userEntity = userService.getAuthenticatedUser();
            
            User userDTO = userMapper.toUserDTO(userEntity);

            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PatchMapping("/me")
    @Operation(
            summary = "Обновление информации об авторизованном пользователе",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateUser.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            },
            tags = "Пользователи"
    )
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UpdateUser> updateUser(@RequestBody UpdateUser updateUser) {
        log.info("Начало обновления информации о пользователе");

        UpdateUser updatedUser = userService.updateUser(updateUser);

        log.info("Информация о пользователе успешно обновлена");
        return ResponseEntity.ok(updateUser);
    }
}