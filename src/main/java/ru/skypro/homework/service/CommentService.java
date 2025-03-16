package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;

import java.util.List;

public interface CommentService {
    List<CommentDTO> getComments(Integer adId);
    CommentDTO addComment(Integer adId, CreateOrUpdateCommentDTO comment);
    void deleteComment(Integer adId, Integer commentId);
    CommentDTO updateComment(Integer adId, Integer commentId, CreateOrUpdateCommentDTO updatedComment);
}