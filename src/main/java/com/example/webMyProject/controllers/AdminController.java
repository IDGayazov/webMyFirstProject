package com.example.webMyProject.controllers;

import com.example.webMyProject.models.Card;
import com.example.webMyProject.models.User;
import com.example.webMyProject.models.UserCard;
import com.example.webMyProject.repository.CardRepository;
import com.example.webMyProject.repository.UserCardRepository;
import com.example.webMyProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {
    @Autowired
    CardRepository cardRepository;
    @Autowired
    UserCardRepository userCardRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/admin")
    public String adminPage(@ModelAttribute("card") Card card,
                            Model model) {
        Iterable<Card> cards = cardRepository.findByIsGlobal(true);
        model.addAttribute("cards", cards);
        return "admin-page";
    }

    @GetMapping("/admin/search")
    public String adminPageSearch(@RequestParam String word,
                                  @ModelAttribute("card") Card card,
                                  Model model) {
        Iterable<Card> cards;
        if(word != null && !word.isEmpty())
            cards = cardRepository.findByRusOrTat(word, word);
        else
            cards = cardRepository.findByIsGlobal(true);
        model.addAttribute("cards", cards);
        return "admin-page";
    }

    @PostMapping("/admin/add")
    public String adminAdd(
            @Valid Card card,
            BindingResult bindingResult,
            Model model
    ){
        if(bindingResult.hasErrors()){
            var errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            model.addAttribute("card", card);
            Iterable<Card> cards = cardRepository.findByIsGlobal(true);
            model.addAttribute("cards", cards);
            return "admin-page";
        }else{
            Card card1 = cardRepository.findByRusAndTat(card.getRus(), card.getTat());
            if(card1 != null){
                Iterable<Card> cards = cardRepository.findByIsGlobal(true);
                model.addAttribute("cards", cards);
                model.addAttribute("message", "Такая карта уже добавлена!");
                return "admin-page";
            }
            User user = userRepository.findByLogin("admin");
            card.setGlobal(true);
            cardRepository.save(card);
            Card card2 = cardRepository.findByRus(card.getRus());
            LocalDate localDate = LocalDate.now(ZoneId.of("Europe/Moscow"));
            Date date = new Date(localDate.getYear() - 1900, localDate.getMonthValue(), localDate.getDayOfMonth());
            UserCard userCard = new UserCard(card2.getId(), user.getId(), date, true);
            System.out.println(date);
            userCardRepository.save(userCard);
        }
        return "redirect:/admin";
    }

    @GetMapping("/admin/{id}")
    public String adminEditPage(@PathVariable(value="id") long id, Model model) {

        if(!cardRepository.existsById(id)){
            return "redirect:/admin";
        }
        Optional<Card> post = cardRepository.findById(id);
        Card res = post.get();
        model.addAttribute("card", res);
        return "admin-edit";
    }

    @PostMapping("/admin/{id}/edit")
    public String adminCardUpdate(@PathVariable(value="id") long id,
                                 @Valid Card card,
                                 BindingResult bindingResult,
                                 Model model) {
        Card card1 = cardRepository.findById(id).orElseThrow();
        if(bindingResult.hasErrors()){
            var errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "admin-edit";
        }else{
            card1.setTat(card.getTat());
            card1.setRus(card.getRus());
            card1.setImage(card.getImage());
            card1.setTag(card.getTag());
            card1.setGlobal(true);
            Card card2 = cardRepository.findByRusAndTat(card.getRus(), card.getTat());
            if(card2 != null){
                model.addAttribute("message", "Такая карта уже добавлена!");
                return "admin-edit";
            }
            cardRepository.save(card1);
            return "redirect:/admin";
        }
    }

    @PostMapping("/admin/{id}/remove")
    public String adminCardDelete(@PathVariable(value="id") long id,
                                 Model model) {
        Card card = cardRepository.findById(id).orElseThrow();
        cardRepository.delete(card);
        return "redirect:/admin";
    }

    private Map<String, String> getErrors(BindingResult result){
        var errors = result.getFieldErrors().stream().collect(Collectors.
                toMap(fieldError -> fieldError.getField() + "Error",
                        FieldError::getDefaultMessage));
        return errors;
    }
}
