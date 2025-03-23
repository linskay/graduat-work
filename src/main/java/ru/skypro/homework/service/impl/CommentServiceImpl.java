package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.AdSecurityService;
import ru.skypro.homework.service.CommentSecurityService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.util.AuthenticationUtils;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final AdRepository adRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final AuthenticationUtils authenticationUtils;
    private final CommentSecurityService commentSecurityService;
    private final AdSecurityService adSecurityService;

    private UserEntity getCurrentUser() {
        return authenticationUtils.getAuthenticatedUser();
    }

    @Override
    public Comment addComment(Integer adId, CreateOrUpdateComment commentDTO) {
        log.debug("Запрос на добавление комментария к объявлению с ID: {}", adId);

        AdEntity adEntity = adRepository.findById(adId)
                .orElseThrow(() -> {
                    log.error("Объявление с ID {} не найдено", adId);
                    return new AdNotFoundException("Объявление не найдено");
                });

        UserEntity currentUser = getCurrentUser();

        CommentEntity commentEntity = commentMapper.toEntity(commentDTO);
        commentEntity.setAuthor(currentUser);
        commentEntity.setAdEntity(adEntity);
        commentEntity.setCreatedAt(Instant.now());

        CommentEntity savedComment = commentRepository.save(commentEntity);

        log.info("Комментарий успешно добавлен к объявлению с ID: {}", adId);
        return commentMapper.toDTO(savedComment);
    }

    @Override
    public Comment updateComment(Integer commentId, CreateOrUpdateComment updateDTO) {
        log.debug("Запрос на обновление комментария с ID: {}", commentId);

        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Комментарий не найден"));

        commentEntity.setText(updateDTO.getText());

        CommentEntity updatedComment = commentRepository.save(commentEntity);

        log.info("Комментарий успешно обновлен с ID: {}", commentId);
        return commentMapper.toDTO(updatedComment);
    }

    @Override
    public void deleteComment(Integer commentId) {
        log.debug("Запрос на удаление комментария с ID: {}", commentId);
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Комментарий не найден"));

        commentRepository.delete(commentEntity);

        log.info("Комментарий успешно удален с ID: {}", commentId);
    }

    @Override
    public Comments getCommentsByAdId(Integer adId) {
        log.debug("Запрос на получение комментариев объявления с ID: {}", adId);

        AdEntity adEntity = adRepository.findById(adId)
                .orElseThrow(() -> new AdNotFoundException("Объявление не найдено"));

        List<CommentEntity> commentEntities = commentRepository.findByAdEntity(adEntity);

        List<Comment> commentDTOs = commentEntities.stream()
                .map(commentMapper::toDTO)
                .toList();

        log.info("Комментарии успешно получены для объявления с ID: {}", adId);
        return Comments.builder()
                .count(commentDTOs.size())
                .results(commentDTOs)
                .build();
    }
}
