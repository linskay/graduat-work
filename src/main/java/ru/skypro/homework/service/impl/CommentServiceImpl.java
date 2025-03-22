package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.exception.*;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final AdRepository adRepository;
    private final CommentMapper commentMapper;

//    @Override
//    public List<CommentEntity> getComments(Integer adId) {
//        logger.info("Fetching comments for ad with id: {}", adId);
//        return commentMapper.commentsToCommentDTOs(commentRepository.findByAdId(adId));
//    }

//    @Override
//    public Comment addComment(Integer adId, CreateOrUpdateComment commentDTO) {
//        UserEntity currentUserEntity = userService.getAuthenticatedUser();
//        logger.info("Adding new comment by user: {}", currentUserEntity.getEmail());
//
//        AdEntity adEntity = adRepository.findById(adId)
//                .orElseThrow(() -> {
//                    logger.error("Ad not found with id: {}", adId);
//                    return new AdNotFoundException("Объявление не найдено");
//                });
//
//        CommentEntity commentEntity = commentMapper.createOrUpdateCommentDTOToComment(commentDTO);
//        commentEntity.setAuthor(currentUserEntity);
//        commentEntity.setAdEntity(adEntity);
//        commentEntity.setCreatedAt(Instant.now());
//
//        CommentEntity savedCommentEntity = commentRepository.save(commentEntity);
//        logger.info("Comment added successfully with id: {}", savedCommentEntity.getId());
//
//        return commentMapper.commentToCommentDTO(savedCommentEntity);
//    }
//
//    @Override
//    public void deleteComment(Integer adId, Integer commentId) {
//        logger.info("Deleting comment with id: {} for ad with id: {}", commentId, adId);
//
//        CommentEntity commentEntity = commentRepository.findById(commentId)
//                .orElseThrow(() -> {
//                    logger.error(ErrorMessages.COMMENT_NOT_FOUND);
//                    return new CommentNotFoundException(ErrorMessages.COMMENT_NOT_FOUND);
//                });
//
//        if (!commentEntity.getAdEntity().getId().equals(adId)) {
//            logger.error(ErrorMessages.COMMENT_NOT_BELONG_TO_AD, commentId, adId);
//            throw new CommentNotBelongToAdException("Комментарий не относится к указанному объявлению");
//        }
//
//        UserEntity currentUserEntity = userService.getAuthenticatedUser();
//
//        if (!commentEntity.getAuthor().equals(currentUserEntity) && !isAdmin(currentUserEntity)) {
//            logger.error("Unauthorized access to delete comment by user: {}", currentUserEntity.getEmail());
//            throw new UnauthorizedAccessException("У вас нет прав для удаления этого комментария");
//        }
//
//        commentRepository.delete(commentEntity);
//        logger.info("Comment deleted successfully with id: {}", commentId);
//    }
//
//    @Override
//    public Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment updatedCommentDTO) {
//        logger.info("Updating comment with id: {} for ad with id: {}", commentId, adId);
//
//        CommentEntity commentEntity = commentRepository.findById(commentId)
//                .orElseThrow(() -> {
//                    logger.error("Comment not found with id: {}", commentId);
//                    return new CommentNotFoundException("Комментарий не найден");
//                });
//
//        if (!commentEntity.getAdEntity().getId().equals(adId)) {
//            logger.error("Comment with id: {} does not belong to ad with id: {}", commentId, adId);
//            throw new CommentNotBelongToAdException("Комментарий не относится к указанному объявлению");
//        }
//
//        UserEntity currentUserEntity = userService.getAuthenticatedUser();
//
//        if (!commentEntity.getAuthor().equals(currentUserEntity) && !isAdmin(currentUserEntity)) {
//            logger.error("Unauthorized access to update comment by user: {}", currentUserEntity.getEmail());
//            throw new UnauthorizedAccessException("У вас нет прав для редактирования этого комментария");
//        }
//
//        CommentEntity updatedCommentEntity = commentMapper.createOrUpdateCommentDTOToComment(updatedCommentDTO);
//        updatedCommentEntity.setId(commentEntity.getId());
//        updatedCommentEntity.setAuthor(commentEntity.getAuthor());
//        updatedCommentEntity.setAdEntity(commentEntity.getAdEntity());
//        updatedCommentEntity.setCreatedAt(commentEntity.getCreatedAt());
//
//        CommentEntity savedCommentEntity = commentRepository.save(updatedCommentEntity);
//        logger.info("Comment updated successfully with id: {}", commentId);
//
//        return commentMapper.commentToCommentDTO(savedCommentEntity);
//    }
//

    private boolean isAdmin(UserEntity userEntity) {
        return userEntity.getRole() == Role.ADMIN;
    }
}