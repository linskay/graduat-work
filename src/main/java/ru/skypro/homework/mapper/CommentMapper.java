package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.CommentEntity;

import java.time.Instant;

@Mapper(componentModel = "spring", uses = {UserMapper.class, AdMapper.class})
public interface CommentMapper {

    @Named("mapInstantToEpochMillis")
    default Long mapInstantToEpochMillis(Instant instant) {
        return (instant != null) ? instant.toEpochMilli() : null;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "adEntity", ignore = true)
    CommentEntity toEntity(CreateOrUpdateComment dto);

    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "author.imageUrl", target = "authorImage")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "mapInstantToEpochMillis")
    Comment toDTO(CommentEntity commentEntity);

}