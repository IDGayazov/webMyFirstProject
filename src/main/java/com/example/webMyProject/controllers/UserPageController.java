package com.example.webMyProject.controllers;

import com.example.webMyProject.models.Card;
import com.example.webMyProject.models.User;
import com.example.webMyProject.models.UserCard;
import com.example.webMyProject.repository.CardRepository;
import com.example.webMyProject.repository.UserCardRepository;
import com.example.webMyProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class UserPageController {
    static private boolean isSession=false;
    @Autowired
    CardRepository cardRepository;

//    @Autowired
//    HttpSessionBean httpSessionBean;

    @Autowired
    UserCardRepository userCardRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/profile")
    public String userPage(HttpSession httpSession, Authentication authentication, Model model){

        String login = authentication.getName();
        User user = userRepository.findByLogin(login);
        long countLearned = 0;
        long countInProcess = 0;
        long countLearnedToday = 0;

//        int count = httpSession.getAttribute("visitCount") != null
//                ? (Integer)httpSession.getAttribute("visitCount")
//                : 0;
//        System.out.println(httpSession.getAttribute("visitCount") );
//        if(!isSession){
//            System.out.println(1);
//            httpSession.setAttribute("visitCount", count + 1);
//            isSession = true;
//        }
//        model.addAttribute("count", count);

        LocalDate localDate = LocalDate.now(ZoneId.of("Europe/Moscow"));
        Date date = new Date(localDate.getYear() - 1900, localDate.getMonthValue(), localDate.getDayOfMonth());

        /////////////////////////////////////////////////
        List<UserCard> userCards = userCardRepository.findByUserId(user.getId());
        countLearned = userCards.stream()
                .filter(UserCard::isLearn)
                .count();
        countInProcess = userCards.stream()
                .filter(c->!c.isLearn())
                .count();
        countLearnedToday = userCards.stream()
                .filter(c -> date.equals(c.getDate()))
                .count();
        model.addAttribute("today", countLearnedToday);
        model.addAttribute("learned", countLearned);
        model.addAttribute("process", countInProcess);
        /////////////////////////////////////////////////

        model.addAttribute("login", login);
        model.addAttribute("email", user.getEmail());
        model.addAttribute("cards", user.getCards());
        return "profile";
    }

    @GetMapping("/profile/add")
    public String cardAddPageShow(Card card,
                                  Model model){
        model.addAttribute("card", card);
        return "profile-add";
    }

    @PostMapping("/profile/add")
    public String cardAddPage(
            Authentication authentication,
            @Valid Card card,
            BindingResult bindingResult,
            Model model
    ){
        if(bindingResult.hasErrors()){
            var errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            model.addAttribute("card", card);
            Iterable<Card> cards = cardRepository.findAll();
            model.addAttribute("cards", cards);
            return "profile";
        }else{
            Card card1 = cardRepository.findByRusAndTat(card.getRus(), card.getTat());
            if(card1 != null){
                Iterable<Card> cards = cardRepository.findAll();
                model.addAttribute("cards", cards);
                model.addAttribute("message", "Такая карта уже добавлена!");
                return "profile-add";
            }
            User user = userRepository.findByLogin(authentication.getName());
            card.setGlobal(false);
            cardRepository.save(card);
            Card card2 = cardRepository.findByRus(card.getRus());
            UserCard userCard = new UserCard(card2.getId(), user.getId(), null, false);
            userCardRepository.save(userCard);
        }
        return "redirect:/profile";
    }

    @GetMapping("/profile/search")
    public String profilePageSearch(Authentication authentication,
                                    @RequestParam String word,
                                    @ModelAttribute("card") Card card,
                                    Model model) {
        Iterable<Card> cards;
        if(word != null && !word.isEmpty()) {
            cards = userRepository.findByLogin(authentication.getName())
                    .getCards()
                    .stream()
                    .filter(c->(c.getRus().equals(word) || c.getTat().equals(word)))
                    .toList();
            if(((List<Card>)cards).isEmpty())
                cards = cardRepository.findByIsGlobal(false);
        }else {
            cards = cardRepository.findByIsGlobal(false);
        }
        model.addAttribute("cards", cards);
        return "profile";
    }

    @GetMapping("/profile/{id}")
    public String profileEditPage(@PathVariable(value="id") long id, Model model) {

        if(!cardRepository.existsById(id)){
            return "redirect:/admin";
        }
        Optional<Card> post = cardRepository.findById(id);
        Card res = post.get();
        model.addAttribute("card", res);
        return "edit";
    }

    @PostMapping("/profile/{id}/edit")
    public String profileCardUpdate(@PathVariable(value="id") long id,
                                  @Valid Card card,
                                  BindingResult bindingResult,
                                  Model model) {
        Card card1 = cardRepository.findById(id).orElseThrow();
        if(bindingResult.hasErrors()){
            var errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "edit";
        }else{
            card1.setTat(card.getTat());
            card1.setRus(card.getRus());
            card1.setImage(card.getImage());
            card1.setTag(card.getTag());
            card1.setGlobal(true);
            Card card2 = cardRepository.findByRusAndTat(card.getRus(), card.getTat());
            if(card2 != null){
                model.addAttribute("message", "Такая карта уже добавлена!");
                return "edit";
            }
            cardRepository.save(card1);
            return "redirect:/profile";
        }
    }

    @PostMapping("/profile/{id}/remove")
    public String profileCardDelete(@PathVariable(value="id") long id,
                                  Model model) {
        Card card = cardRepository.findById(id).orElseThrow();
        cardRepository.delete(card);
        return "redirect:/profile";
    }

    private Map<String, String> getErrors(BindingResult result) {
        var errors = result.getFieldErrors().stream().collect(Collectors.
                toMap(fieldError -> fieldError.getField() + "Error",
                        FieldError::getDefaultMessage));
        return errors;
    }
}
