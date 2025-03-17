package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.CommentNotBelongToAdException;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.exception.UnauthorizedAccessException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final AdRepository adRepository;
    private final CommentMapper commentMapper;

    @Override
    public List<Comment> getComments(Integer adId) {
        logger.info("Fetching comments for ad with id: {}", adId);
        List<ru.skypro.homework.model.Comment> comments = commentRepository.findByAdId(adId);
        logger.info("Found {} comments for ad with id: {}", comments.size(), adId);
        return commentMapper.commentsToCommentDTOs(comments);
    }

    @Override
    public Comment addComment(Integer adId, CreateOrUpdateComment commentDTO) {
        User currentUser = userService.getCurrentUser();
        logger.info("Adding new comment by user: {}", currentUser.getUsername());

        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> {
                    logger.error("Ad not found with id: {}", adId);
                    return new AdNotFoundException("Объявление не найдено");
                });

        ru.skypro.homework.model.Comment comment = commentMapper.createOrUpdateCommentDTOToComment(commentDTO);
        comment.setAuthor(currentUser);
        comment.setAd(ad);
        comment.setCreatedAt(Instant.now());

        ru.skypro.homework.model.Comment savedComment = commentRepository.save(comment);
        logger.info("Comment added successfully with id: {}", savedComment.getId());

        return commentMapper.commentToCommentDTO(savedComment);
    }

    @Override
    public void deleteComment(Integer adId, Integer commentId) {
        logger.info("Deleting comment with id: {} for ad with id: {}", commentId, adId);

        ru.skypro.homework.model.Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    logger.error("Comment not found with id: {}", commentId);
                    return new CommentNotFoundException("Комментарий не найден");
                });

        if (!comment.getAd().getId().equals(adId)) {
            logger.error("Comment with id: {} does not belong to ad with id: {}", commentId, adId);
            throw new CommentNotBelongToAdException("Комментарий не относится к указанному объявлению");
        }

        User currentUser = userService.getCurrentUser();

        if (!comment.getAuthor().equals(currentUser) && !isAdmin(currentUser)) {
            logger.error("Unauthorized access to delete comment by user: {}", currentUser.getUsername());
            throw new UnauthorizedAccessException("У вас нет прав для удаления этого комментария");
        }

        commentRepository.delete(comment);
        logger.info("Comment deleted successfully with id: {}", commentId);
    }

    @Override
    public Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment updatedCommentDTO) {
        logger.info("Updating comment with id: {} for ad with id: {}", commentId, adId);

        ru.skypro.homework.model.Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    logger.error("Comment not found with id: {}", commentId);
                    return new CommentNotFoundException("Комментарий не найден");
                });

        if (!comment.getAd().getId().equals(adId)) {
            logger.error("Comment with id: {} does not belong to ad with id: {}", commentId, adId);
            throw new CommentNotBelongToAdException("Комментарий не относится к указанному объявлению");
        }

        User currentUser = userService.getCurrentUser();

        if (!comment.getAuthor().equals(currentUser) && !isAdmin(currentUser)) {
            logger.error("Unauthorized access to update comment by user: {}", currentUser.getUsername());
            throw new UnauthorizedAccessException("У вас нет прав для редактирования этого комментария");
        }

        ru.skypro.homework.model.Comment updatedComment = commentMapper.createOrUpdateCommentDTOToComment(updatedCommentDTO);
        updatedComment.setId(comment.getId());
        updatedComment.setAuthor(comment.getAuthor());
        updatedComment.setAd(comment.getAd());
        updatedComment.setCreatedAt(comment.getCreatedAt());

        ru.skypro.homework.model.Comment savedComment = commentRepository.save(updatedComment);
        logger.info("Comment updated successfully with id: {}", commentId);

        return commentMapper.commentToCommentDTO(savedComment);
    }

    /**
     * Проверяет, является ли пользователь админом.
     *
     * @param user пользователь
     * @return true, если пользователь — админ, иначе false
     */
    private boolean isAdmin(User user) {
        return user.getRole() == Role.ADMIN;
    }
}