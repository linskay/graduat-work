package ru.skypro.homework.service;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.CommentEntity;

import java.util.List;

public interface CommentService {
   // List<CommentEntity> getComments(Integer adId);
    Comment addComment(Integer adId, CreateOrUpdateComment comment);
    void deleteComment(Integer adId, Integer commentId);
    Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment updatedComment);
}