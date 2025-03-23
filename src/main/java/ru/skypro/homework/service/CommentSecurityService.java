package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.util.AuthenticationUtils;

@Service
@RequiredArgsConstructor
public class CommentSecurityService {

    private final CommentRepository commentRepository;
    private final AuthenticationUtils authenticationUtils;

    public boolean isAuthor(Integer commentId) {
        return commentRepository.findById(commentId)
                .map(commentEntity -> commentEntity.getAuthor().equals(authenticationUtils.getAuthenticatedUser()))
                .orElse(false);
    }

    public boolean hasPermissionToUpdate(Integer commentId) {
        UserEntity currentUser = authenticationUtils.getAuthenticatedUser();
        return isAuthor(commentId) || isAdmin(currentUser);
    }

    private boolean isAdmin(UserEntity user) {
        return user.getRole() == Role.ADMIN;
    }
}