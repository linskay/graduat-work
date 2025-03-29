package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdRepository extends JpaRepository<AdEntity, Integer> {
    List<AdEntity> findByAuthor_Email(String email);
}