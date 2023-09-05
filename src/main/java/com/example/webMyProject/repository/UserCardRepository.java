package com.example.webMyProject.repository;

import com.example.webMyProject.models.UserCard;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;

public interface UserCardRepository extends CrudRepository<UserCard, Long> {
    UserCard findByCardIdAndUserId(long cardId, long userId);
    List<UserCard> findByUserId(long userId);
}
