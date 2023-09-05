package com.example.webMyProject.controllers;

import com.example.webMyProject.models.Card;
import com.example.webMyProject.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class MainPageController {
    @Autowired
    CardRepository cardRepository;

    @GetMapping("/main")
    public String mainPage(Model model){
        Iterable<Card> cds = cardRepository.findByIsGlobal(true);
        List<Card> cards = new ArrayList<>();
        for(Card c: cds){
            cards.add(c);
        }
        Map<String, List<Card>> map = cards.stream()
                        .collect(Collectors.groupingBy(Card::getTag));
        model.addAttribute("map", map);
        return "main_page.html";
    }
}
