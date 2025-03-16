package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AdRepository adRepository;
    private final CommentMapper commentMapper;

    @Override
    public List<Comment> getComments(Integer adId) {
        return commentMapper.commentsToCommentDTOs(commentRepository.findByAdId(adId));
    }

    @Override
    public Comment addComment(Integer adId, CreateOrUpdateComment commentDTO) {
        User currentUser = getCurrentUser();

        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));

        ru.skypro.homework.model.Comment comment = commentMapper.createOrUpdateCommentDTOToComment(commentDTO);
        comment.setAuthor(currentUser);
        comment.setAd(ad);
        comment.setCreatedAt(Instant.now());

        ru.skypro.homework.model.Comment savedComment = commentRepository.save(comment);

        return commentMapper.commentToCommentDTO(savedComment);
    }

    @Override
    public void deleteComment(Integer adId, Integer commentId) {
        ru.skypro.homework.model.Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Комментарий не найден"));

        if (!comment.getAd().getId().equals(adId)) {
            throw new RuntimeException("Комментарий не относится к указанному объявлению");
        }

        commentRepository.delete(comment);
    }

    @Override
    public Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment updatedCommentDTO) {
        ru.skypro.homework.model.Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Комментарий не найден"));

        if (!comment.getAd().getId().equals(adId)) {
            throw new RuntimeException("Комментарий не относится к указанному объявлению");
        }

        ru.skypro.homework.model.Comment updatedComment = commentMapper.createOrUpdateCommentDTOToComment(updatedCommentDTO);
        updatedComment.setId(comment.getId());
        updatedComment.setAuthor(comment.getAuthor());
        updatedComment.setAd(comment.getAd());
        updatedComment.setCreatedAt(comment.getCreatedAt());

        ru.skypro.homework.model.Comment savedComment = commentRepository.save(updatedComment);

        return commentMapper.commentToCommentDTO(savedComment);
    }

    private User getCurrentUser() {
        // toDo: Логика получения текущего авторизованного пользователя
        return userRepository.findById(1L).orElseThrow(() -> new RuntimeException("Пользователь не авторизован"));
    }
}