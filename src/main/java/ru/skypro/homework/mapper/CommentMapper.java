package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.model.Comment;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Named("mapInstantToEpochMillis")
    default Long mapInstantToEpochMillis(Instant instant) {
        return (instant != null) ? instant.toEpochMilli() : null;
    }

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "author.firstName", target = "authorUsername")
    @Mapping(source = "author.imageUrl", target = "authorImage")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "mapInstantToEpochMillis")
    CommentDTO commentToCommentDTO(Comment comment);

    List<CommentDTO> commentsToCommentDTOs(List<Comment> comments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "ad", ignore = true)
    Comment createOrUpdateCommentDTOToComment(CreateOrUpdateCommentDTO dto);
}