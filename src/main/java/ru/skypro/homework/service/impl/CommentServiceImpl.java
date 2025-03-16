package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public List<CommentDTO> getComments(Integer adId) {
        return commentRepository.findByAdId(adId).stream()
                .map(this::toCommentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDTO addComment(Integer adId, CreateOrUpdateCommentDTO comment) {

        Comment newComment = new Comment();
        newComment.setText(comment.getText());
        newComment.setCreatedAt(Instant.now());
        //toDO id автора и объявление (ad), вывести в комментарий

        Comment savedComment = commentRepository.save(newComment);

        return toCommentDTO(savedComment);
    }

    @Override
    public void deleteComment(Integer adId, Integer commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentDTO updateComment(Integer adId, Integer commentId, CreateOrUpdateCommentDTO updatedComment) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Комментарий не найден"));

        comment.setText(updatedComment.getText());

        Comment updated = commentRepository.save(comment);

        return toCommentDTO(updated);
    }

    private CommentDTO toCommentDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setPk(comment.getId());
        dto.setText(comment.getText());

        if (comment.getAuthor() != null) {
            dto.setAuthor(comment.getAuthor().getId());
            dto.setAuthorUsername(comment.getAuthor().getUsername());
            dto.setAuthorImage(comment.getAuthor().getImageUrl());
        } else {
            dto.setAuthor(null);
            dto.setAuthorUsername("Unknown");
            dto.setAuthorImage(null);
        }

        if (comment.getCreatedAt() != null) {
            dto.setCreatedAt(comment.getCreatedAt().toEpochMilli());
        } else {
            dto.setCreatedAt(0L);
        }
        return dto;
    }
}