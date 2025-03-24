package ru.skypro.homework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

@Configuration
public class JdbcUserDetailsConfig {

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource, PasswordEncoder passwordEncoder) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

        manager.setUsersByUsernameQuery("SELECT email, password, enabled FROM users WHERE email = ?");
        manager.setAuthoritiesByUsernameQuery("SELECT email, authority FROM authorities WHERE email = ?");
        manager.setCreateAuthoritySql("INSERT INTO authorities (email, authority) VALUES (?, ?)");
        manager.setCreateUserSql("INSERT INTO users (email, password, enabled, first_name, last_name, phone, role) VALUES (?, ?, ?, ?, ?, ?, ?)");

        return manager;
    }
}