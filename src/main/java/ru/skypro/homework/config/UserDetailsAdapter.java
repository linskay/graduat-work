package ru.skypro.homework.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.model.UserEntity;

import java.util.Collection;
import java.util.Collections;

public record UserDetailsAdapter(UserEntity user) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList((GrantedAuthority) () -> "ROLE_" + user.getRole());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}