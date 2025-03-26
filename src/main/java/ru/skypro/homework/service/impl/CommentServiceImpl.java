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


    private UserEntity getCurrentUser() {
        return authenticationUtils.getAuthenticatedUser();
    }

    @Override
    public Comment addComment(Integer adId, CreateOrUpdateComment commentDTO) {

        AdEntity adEntity = adRepository.findById(adId)
                .orElseThrow(() -> new AdNotFoundException(adId));

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
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        commentEntity.setText(updateDTO.getText());

        CommentEntity updatedComment = commentRepository.save(commentEntity);
        log.info("Комментарий успешно обновлен с ID: {}", commentId);

        return commentMapper.toDTO(updatedComment);
    }

    @Override
    public void deleteComment(Integer commentId) {
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new CommentNotFoundException(commentId));
        commentRepository.delete(commentEntity);
    }

    @Override
    public Comments getCommentsByAdId(Integer adId) {

        AdEntity adEntity = adRepository.findById(adId)
                .orElseThrow(() ->
                        new AdNotFoundException(adId));
        List<CommentEntity> commentEntities = commentRepository.findByAdEntity(adEntity);

        List<Comment> commentDTOs = commentEntities.stream()
                .map(commentMapper::toDTO)
                .toList();

        return Comments.builder()
                .count(commentDTOs.size())
                .results(commentDTOs)
                .build();
    }
}
