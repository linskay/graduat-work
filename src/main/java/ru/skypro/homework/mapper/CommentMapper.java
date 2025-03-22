package ru.skypro.homework.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.UserEntity;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, AdMapper.class})
public interface CommentMapper {

    @Named("mapInstantToEpochMillis")
    default Long mapInstantToEpochMillis(Instant instant) {
        return (instant != null) ? instant.toEpochMilli() : null;
    }

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "author.firstName", target = "authorUsername")
    @Mapping(source = "author.imageUrl", target = "authorImage")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "mapInstantToEpochMillis")
  //  @Mapping(source = "adEntity.id", target = "ad")
    Comment commentToCommentDTO(CommentEntity commentEntity);

    List<Comment> commentsToCommentDTOs(List<CommentEntity> commentEntities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "author", ignore = true)
 //   @Mapping(target = "ad", ignore = true)
    CommentEntity createOrUpdateCommentDTOToComment(CreateOrUpdateComment dto);
//
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "createdAt", ignore = true)
//    @Mapping(target = "author", ignore = true)
//    @Mapping(target = "adEntity", ignore = true)
//    CommentEntity createOrUpdateCommentDTOToComment(CreateOrUpdateComment dto, @Context UserEntity author, @Context AdEntity adEntity);
}