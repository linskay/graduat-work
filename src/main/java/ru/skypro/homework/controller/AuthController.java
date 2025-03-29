package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collections;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Tag(name = "Аутентификация", description = "Операции для регистрации и авторизации пользователей")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(
            summary = "Авторизация пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
            }
    )
    public ResponseEntity<?> login(@RequestBody @Valid Login login,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        request.getSession(true);

        return ResponseEntity.ok()
                .header("Access-Control-Allow-Credentials", "true")
                .body(Collections.singletonMap("status", "Logged in"));
    }


    @PostMapping("/register")
    @Operation(
            summary = "Регистрация пользователя",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created", content = @Content()),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content())
            }
    )
    public ResponseEntity<Long> register(@RequestBody @Valid Register register) {
        Long userId = authService.register(register);
        return new ResponseEntity<>(userId, HttpStatus.CREATED);
    }
}