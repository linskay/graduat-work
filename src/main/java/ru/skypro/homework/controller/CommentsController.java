package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
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
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    @PreAuthorize("isAuthenticated()")

    public ResponseEntity<Comment> addComment(
            @PathVariable Integer id,
            @RequestBody @Valid CreateOrUpdateComment commentDTO) {

        log.debug("Запрос на добавление комментария к объявлению с ID: {}", id);

        Comment createdComment = commentService.addComment(id, commentDTO);

        log.info("Комментарий успешно добавлен к объявлению с ID: {}", id);
        return ResponseEntity.ok(createdComment);
    }

    @Operation(
            summary = "Обновление комментария",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    @PreAuthorize("isAuthenticated() and (@commentSecurityService.hasPermissionToUpdate(#commentId))")
    @PutMapping(value = "/{commentId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Comment> updateComment(
            @PathVariable Integer adId,
            @PathVariable Integer commentId,
            @RequestBody @Valid CreateOrUpdateComment updateDTO) {

        log.debug("Запрос на обновление комментария с ID: {}", commentId);

        Comment updatedComment = commentService.updateComment(commentId, updateDTO);

        log.info("Комментарий успешно обновлен с ID: {}", commentId);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{commentId}")
    @Operation(
            summary = "Удаление комментария",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    @PreAuthorize("isAuthenticated() and (@commentSecurityService.hasPermissionToUpdate(#commentId))")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer commentId) {
        log.debug("Запрос на удаление комментария с ID: {}", commentId);
        commentService.deleteComment(commentId);
        log.info("Комментарий успешно удален с ID: {}", commentId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получение комментариев объявления")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Comments> getComments(@PathVariable Integer id) {
        log.debug("Запрос на получение комментариев объявления с ID: {}", id);

        Comments comments = commentService.getCommentsByAdId(id);

        log.info("Комментарии успешно получены для объявления с ID: {}", id);
        return ResponseEntity.ok(comments);
    }
}