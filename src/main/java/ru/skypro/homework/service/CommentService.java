package ru.skypro.homework.service;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;

import java.util.List;

public interface CommentService {
    List<Comment> getComments(Integer adId);
    Comment addComment(Integer adId, CreateOrUpdateComment comment);
    void deleteComment(Integer adId, Integer commentId);
    Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment updatedComment);
}