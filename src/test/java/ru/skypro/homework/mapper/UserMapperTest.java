package ru.skypro.homework.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.model.UserEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;
    private UserEntity userEntity;
    private User userDTO;
    private Login login;
    private UpdateUser updateUser;

    public UserMapperTest() {
        this.userMapper = Mappers.getMapper(UserMapper.class);
    }

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Иван");
        userEntity.setLastName("Иванов");
        userEntity.setEmail("ivan@example.com");
        userEntity.setPassword("hashedPassword");
        userEntity.setPhone("+79251234567");
        userEntity.setImageUrl("default-avatar.jpg");

        userDTO = new User();
        userDTO.setId(1L);
        userDTO.setFirstName("Иван");
        userDTO.setLastName("Иванов");
        userDTO.setEmail("ivan@example.com");
        userDTO.setPhone("+79251234567");
        userDTO.setImageUrl("default-avatar.jpg");

        login = new Login();
        login.setUsername("user@example.com");
        login.setPassword("password123");

        Register register = new Register();
        register.setUsername("user@example.com");
        register.setPassword("password123");
        register.setFirstName("Иван");
        register.setLastName("Иванов");
        register.setPhone("+79251234567");
        register.setRole(Role.valueOf("USER"));

        updateUser = new UpdateUser();
        updateUser.setFirstName("Петр");
        updateUser.setLastName("Петров");
        updateUser.setPhone("+79259876543");
    }

    @Test
    @DisplayName("Проверка преобразования UserEntity в User")
    void testToUserDTO() {
        User result = userMapper.toUserDTO(userEntity);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userEntity.getId());
        assertThat(result.getFirstName()).isEqualTo(userEntity.getFirstName());
        assertThat(result.getLastName()).isEqualTo(userEntity.getLastName());
        assertThat(result.getEmail()).isEqualTo(userEntity.getEmail());
        assertThat(result.getPhone()).isEqualTo(userEntity.getPhone());
        assertThat(result.getImageUrl()).isEqualTo(userEntity.getImageUrl());
    }

    @Test
    @DisplayName("Проверка преобразования User в UserEntity")
    void testToUser() {
        UserEntity result = userMapper.toUser(userDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userDTO.getId());
        assertThat(result.getFirstName()).isEqualTo(userDTO.getFirstName());
        assertThat(result.getLastName()).isEqualTo(userDTO.getLastName());
        assertThat(result.getEmail()).isEqualTo(userDTO.getEmail());
        assertThat(result.getPhone()).isEqualTo(userDTO.getPhone());
        assertThat(result.getImageUrl()).isEqualTo(userDTO.getImageUrl());
    }

    @Test
    @DisplayName("Проверка преобразования Login в UserEntity")
    void testToUserFromLogin() {
        UserEntity result = userMapper.toUser(login);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(login.getUsername());
        assertThat(result.getPassword()).isEqualTo(login.getPassword());
    }

    @Test
    @DisplayName("Проверка обновления UserEntity из UpdateUser")
    void testUpdateUserFromDTO() {
        userMapper.updateUserFromDTO(updateUser, userEntity);

        assertThat(userEntity.getFirstName()).isEqualTo(updateUser.getFirstName());
        assertThat(userEntity.getLastName()).isEqualTo(updateUser.getLastName());
        assertThat(userEntity.getPhone()).isEqualTo(updateUser.getPhone());
        assertThat(userEntity.getId()).isEqualTo(1L);
        assertThat(userEntity.getEmail()).isEqualTo("ivan@example.com");
        assertThat(userEntity.getPassword()).isEqualTo("hashedPassword");
        assertThat(userEntity.getImageUrl()).isEqualTo("default-avatar.jpg");
    }
}