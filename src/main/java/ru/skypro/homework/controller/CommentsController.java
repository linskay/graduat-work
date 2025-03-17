package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;

import java.util.List;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads/{id}/comments")
@RequiredArgsConstructor
@Tag(name = "Комментарии", description = "Операции с комментариями")
public class CommentsController {

    private final CommentService commentService;

    @GetMapping
    @Operation(
            summary = "Получение комментариев объявления",
            description = "Возвращает список комментариев для указанного объявления. Доступно всем пользователям.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Comments.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    public Comments getComments(@PathVariable Integer id) {
        log.info("Запрос на получение комментариев для объявления с ID: {}", id);
        List<Comment> comments = commentService.getComments(id);
        Comments response = new Comments();
        response.setCount(comments.size());
        response.setResults(comments);
        return response;
    }

    @PostMapping
    @Operation(
            summary = "Добавление комментария к объявлению",
            description = "Создает новый комментарий для указанного объявления. Доступно только авторизованным пользователям.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Comment.class)
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Integer addComment(
            @PathVariable Integer id,
            @RequestBody CreateOrUpdateComment comment) {
        log.info("Запрос на добавление комментария к объявлению с ID: {}", id);
        Comment createdComment = commentService.addComment(id, comment);
        return createdComment.getPk();
    }

    @DeleteMapping("/{commentId}")
    @Operation(
            summary = "Удаление комментария",
            description = "Удаляет комментарий по ID. Доступно только автору комментария или администратору.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(
            @PathVariable Integer id,
            @PathVariable Integer commentId) {
        log.info("Запрос на удаление комментария с ID: {} для объявления с ID: {}", commentId, id);
        commentService.deleteComment(id, commentId);
    }

    @PatchMapping("/{commentId}")
    @Operation(
            summary = "Обновление комментария",
            description = "Обновляет информацию о комментарии. Доступно только автору комментария или администратору.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Comment.class)
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    public Comment updateComment(
            @PathVariable Integer id,
            @PathVariable Integer commentId,
            @RequestBody CreateOrUpdateComment updatedComment) {
        log.info("Запрос на обновление комментария с ID: {} для объявления с ID: {}", commentId, id);
        return commentService.updateComment(id, commentId, updatedComment);
    }
}