package ru.skypro.homework.service;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

public interface CommentService {

    Comment addComment(Integer adId, CreateOrUpdateComment commentDTO);

    Comment updateComment(Integer commentId, CreateOrUpdateComment updateDTO);

    void deleteComment(Integer commentId);

    Comments getCommentsByAdId(Integer adId);
}