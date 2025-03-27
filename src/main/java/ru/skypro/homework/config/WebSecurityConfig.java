package ru.skypro.homework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**",
            "/login",
            "/register",
            "/ads"
    };


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests((authz) -> authz
                        .mvcMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest().authenticated()
                )
                .cors()
                .and()
                .httpBasic().disable();
        return http.build();
    }

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource, PasswordEncoder passwordEncoder) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

        manager.setUsersByUsernameQuery("SELECT email, password, enabled FROM users WHERE email = ?");
        manager.setAuthoritiesByUsernameQuery("SELECT email, authority FROM authorities WHERE email = ?");
        manager.setCreateAuthoritySql("INSERT INTO authorities (email, authority) VALUES (?, ?)");
        manager.setCreateUserSql("INSERT INTO users (email, password, enabled, first_name, last_name, phone) VALUES (?, ?, ?, ?, ?, ?)");
        manager.setDeleteUserSql("DELETE FROM users WHERE email = ?");
        manager.setUpdateUserSql("UPDATE users SET password = ?, enabled = ? WHERE email = ?");
        manager.setDeleteUserAuthoritiesSql("DELETE FROM authorities WHERE email = ?");
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}