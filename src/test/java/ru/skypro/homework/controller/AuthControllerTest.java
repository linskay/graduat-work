package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Тестирование контроллера AuthController")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Авторизация пользователя - успешно")
    void login_ShouldReturnOk() throws Exception {
        Login login = new Login();
        login.setUsername("user");
        login.setPassword("password");

        when(authService.login(login.getUsername(), login.getPassword())).thenReturn(true);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Авторизация пользователя - неверные учетные данные")
    void login_ShouldReturnUnauthorized() throws Exception {
        Login login = new Login();
        login.setUsername("user");
        login.setPassword("wrongpassword");

        when(authService.login(login.getUsername(), login.getPassword())).thenReturn(false);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Регистрация пользователя - успешно")
    void register_ShouldReturnCreated() throws Exception {
        Register register = new Register();
        register.setUsername("newuser");
        register.setPassword("password");

        when(authService.register(any(Register.class))).thenReturn(true);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Регистрация пользователя - некорректные данные")
    void register_ShouldReturnBadRequest() throws Exception {
        Register register = new Register();
        register.setUsername("invaliduser");
        register.setPassword("short");

        when(authService.register(any(Register.class))).thenReturn(false);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isBadRequest());
    }
}