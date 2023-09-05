package com.example.webMyProject.repository;

import com.example.webMyProject.models.Card;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CardRepository extends CrudRepository<Card, Long> {
    List<Card> findByRusOrTat(String rus, String tat);
    Card findByRus(String rus);
    Card findByRusAndTat(String rus, String tat);

    List<Card> findByIsGlobal(boolean isGlobal);

    List<Card> findByTagAndIsGlobal(String tag, boolean isGlobal);

    Card findByTat(String tat);

}
