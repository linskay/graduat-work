package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/ads/{id}/comments")
@RequiredArgsConstructor
@Tag(name = "Комментарии", description = "Операции с комментариями")
public class CommentsController {
    private final CommentService commentService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Добавление комментария к объявлению",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Comment.class))),
                    @ApiResponse(responseCode = "401", description = "Не авторизован"),
                    @ApiResponse(responseCode = "404", description = "Не найдено")
            }
    )
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Comment> addComment(
            @PathVariable Integer id,
            @RequestBody @Valid CreateOrUpdateComment commentDTO) {
        Comment createdComment = commentService.addComment(id, commentDTO);
        return ResponseEntity.ok(createdComment);
    }

    @PutMapping(value = "/{commentId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Обновление комментария",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Comment.class))),
                    @ApiResponse(responseCode = "401", description = "Не авторизован"),
                    @ApiResponse(responseCode = "403", description = "Запрещено"),
                    @ApiResponse(responseCode = "404", description = "Не найдено")
            }
    )
    @PreAuthorize("isAuthenticated() and (@commentSecurityService.hasPermissionToUpdate(#commentId))")
    public ResponseEntity<Comment> updateComment(
            @PathVariable Integer commentId,
            @RequestBody @Valid CreateOrUpdateComment updateDTO, @PathVariable String id) {

        Comment updatedComment = commentService.updateComment(commentId, updateDTO);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{commentId}")
    @Operation(
            summary = "Удаление комментария",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Не авторизован"),
                    @ApiResponse(responseCode = "403", description = "Запрещено"),
                    @ApiResponse(responseCode = "404", description = "Не найдено")
            }
    )
    @PreAuthorize("isAuthenticated() and (@commentSecurityService.hasPermissionToUpdate(#commentId))")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer commentId, @PathVariable String id) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Получение комментариев объявления",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Comments.class))),
                    @ApiResponse(responseCode = "401", description = "Не авторизован"),
                    @ApiResponse(responseCode = "404", description = "Не найдено")
            }
    )
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Comments> getComments(@PathVariable Integer id) {
        Comments comments = commentService.getCommentsByAdId(id);
        return ResponseEntity.ok(comments);
    }
}