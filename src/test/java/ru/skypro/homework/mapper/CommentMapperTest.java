package ru.skypro.homework.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.UserEntity;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CommentMapperTest {

    @Autowired
    private CommentMapper commentMapper;

    public CommentMapperTest() {
        this.commentMapper = Mappers.getMapper(CommentMapper.class);
    }

    @Test
    @DisplayName("Проверка преобразования CreateOrUpdateComment в CommentEntity")
    void testToEntity() {
        CreateOrUpdateComment dto = new CreateOrUpdateComment();
        dto.setText("Олег хочет продать топор");

        CommentEntity entity = commentMapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getText()).isEqualTo("Олег хочет продать топор");
        assertThat(entity.getId()).isNull();
        assertThat(entity.getCreatedAt()).isNull();
        assertThat(entity.getAuthor()).isNull();
        assertThat(entity.getAdEntity()).isNull();
    }

    @Test
    @DisplayName("Проверка преобразования CommentEntity в Comment")
    void testToDTO() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setImage("https://avito-for-oleg/avatar.jpg");

        AdEntity ad = new AdEntity();
        ad.setId(1);

        CommentEntity entity = new CommentEntity();
        entity.setId(1);
        entity.setText("Олег хочет продать топор");
        entity.setCreatedAt(Instant.parse("2023-01-01T00:00:00Z"));
        entity.setAuthor(user);
        entity.setAdEntity(ad);

        Comment dto = commentMapper.toDTO(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getPk()).isEqualTo(1);
        assertThat(dto.getText()).isEqualTo("Олег хочет продать топор");
        assertThat(dto.getAuthor()).isEqualTo(1L);
        assertThat(dto.getAuthorImage()).isEqualTo("https://avito-for-oleg/avatar.jpg");
        assertThat(dto.getCreatedAt()).isEqualTo(1672531200000L);
    }
}