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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class TestController {
    @Autowired
    CardRepository cardRepository;

    @Autowired
    UserCardRepository userCardRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/profile/learn")
    public String cardLearnPageShow(HttpSession session,
                                    Model model){

        System.out.println("cardLearnPageShow");

        Card card1 = session.getAttribute("card1") != null
                ?(Card)session.getAttribute("card1")
                :null;
        Card card2 = session.getAttribute("card2") != null
                ?(Card)session.getAttribute("card2")
                :null;
        Card card3 = session.getAttribute("card3") != null
                ?(Card)session.getAttribute("card3")
                :null;
        Card card4 = session.getAttribute("card4") != null
                ?(Card)session.getAttribute("card4")
                :null;

        model.addAttribute("isDone",
                session.getAttribute("isDone"));
        model.addAttribute("isTrue1",
                session.getAttribute("isTrue1"));
        model.addAttribute("isTrue2",
                session.getAttribute("isTrue2"));
        model.addAttribute("isTrue3",
                session.getAttribute("isTrue3"));
        model.addAttribute("isTrue4",
                session.getAttribute("isTrue4"));

        model.addAttribute("card1", card1);
        model.addAttribute("card2", card2);
        model.addAttribute("card3", card3);
        model.addAttribute("card4", card4);

        session.removeAttribute("card1");
        session.removeAttribute("card2");
        session.removeAttribute("card3");
        session.removeAttribute("card4");
        session.removeAttribute("isDone");
        return "learn";
    }

    @PostMapping("/profile/learn")
    public String cardLearnPagePost(
            Authentication authentication,
            HttpSession session,
            @ModelAttribute("rus1") String rus1,
            @ModelAttribute("rus2") String rus2,
            @ModelAttribute("rus3") String rus3,
            @ModelAttribute("rus4") String rus4,
            Model model
    ) {
        System.out.println("cardLearnPagePost");

        Card card1 = session.getAttribute("card1") != null
                ?(Card)session.getAttribute("card1")
                :null;
        Card card2 = session.getAttribute("card2") != null
                ?(Card)session.getAttribute("card2")
                :null;
        Card card3 = session.getAttribute("card3") != null
                ?(Card)session.getAttribute("card3")
                :null;
        Card card4 = session.getAttribute("card4") != null
                ?(Card)session.getAttribute("card4")
                :null;

        if(card1 != null && !card1.getRus().equals(rus1)) {
            session.setAttribute("isTrue1", false);
        }
        else if(card1 != null && card1.getRus().equals(rus1)){
            User user = userRepository.findByLogin(authentication.getName());
            long id = user.getId();
            UserCard userCard = userCardRepository.findByCardIdAndUserId(card1.getId(), id);
            userCard.setLearn(true);
            LocalDate localDate = LocalDate.now(ZoneId.of("Europe/Moscow"));
            Date date = new Date(localDate.getYear() - 1900, localDate.getMonthValue(), localDate.getDayOfMonth());
            userCard.setDate(date);
            userCardRepository.save(userCard);
        }
        if(card2 != null && !card2.getRus().equals(rus2)) {
            session.setAttribute("isTrue2", false);
        }else if(card2 != null && card2.getRus().equals(rus2)){
            User user = userRepository.findByLogin(authentication.getName());
            long id = user.getId();
            UserCard userCard = userCardRepository.findByCardIdAndUserId(card2.getId(), id);
            userCard.setLearn(true);
            LocalDate localDate = LocalDate.now(ZoneId.of("Europe/Moscow"));
            Date date = new Date(localDate.getYear() - 1900, localDate.getMonthValue(), localDate.getDayOfMonth());
            userCard.setDate(date);
            userCardRepository.save(userCard);
        }
        if(card3 != null && !card3.getRus().equals(rus3)) {
            //isTrue3 = false;
            session.setAttribute("isTrue3", false);
        }else if(card3 != null && card3.getRus().equals(rus3)){
            User user = userRepository.findByLogin(authentication.getName());
            long id = user.getId();
            UserCard userCard = userCardRepository.findByCardIdAndUserId(card3.getId(), id);
            userCard.setLearn(true);
            LocalDate localDate = LocalDate.now(ZoneId.of("Europe/Moscow"));
            Date date = new Date(localDate.getYear() - 1900, localDate.getMonthValue(), localDate.getDayOfMonth());
            userCard.setDate(date);
            userCardRepository.save(userCard);
        }
        if(card4 != null && !card4.getRus().equals(rus4)) {
            session.setAttribute("isTrue4", false);
        }else if(card4 != null && card4.getRus().equals(rus4)){
            User user = userRepository.findByLogin(authentication.getName());
            long id = user.getId();
            UserCard userCard = userCardRepository.findByCardIdAndUserId(card4.getId(), id);
            userCard.setLearn(true);
            LocalDate localDate = LocalDate.now(ZoneId.of("Europe/Moscow"));
            Date date = new Date(localDate.getYear() - 1900, localDate.getMonthValue(), localDate.getDayOfMonth());
            userCard.setDate(date);
            userCardRepository.save(userCard);
        }
        session.setAttribute("isDone", true);
        return "redirect:/profile/learn";
    }

    @GetMapping("/profile/learn/search")
    public String cardGetByTag(
            @RequestParam(required = false) String tag,
            HttpSession session,
            @ModelAttribute("rus1") String rus1,
            @ModelAttribute("rus2") String rus2,
            @ModelAttribute("rus3") String rus3,
            @ModelAttribute("rus4") String rus4,
            Model model
    ) {
        Card card1 = null;
        Card card2 = null;
        Card card3 = null;
        Card card4 = null;
//
        List<Card> cards;
        if(tag == null){
            cards = null;
        }
        cards = cardRepository.findByTagAndIsGlobal(tag, false);
//        for(Card card: cards){
//            System.out.println(card.getRus());
//        }
        if(cards.size() == 1){
            card1 = cards.get(0);
        }
        else if(cards.size() == 2){
            card1 = cards.get(0);
            card2 = cards.get(1);
        }
        else if(cards.size() == 3){
            card1 = cards.get(0);
            card2 = cards.get(1);
            card3 = cards.get(2);
        }
        else if(cards.size() == 4){
            card1 = cards.get(0);
            card2 = cards.get(1);
            card3 = cards.get(2);
            card4 = cards.get(3);
        }
        else if(cards.size() > 4){
            Random rand = new Random();
            int n = cards.size();
            List<Integer> randoms = new ArrayList<>(4);
            int i = 0;
            while(i < 4){
                int x = rand.nextInt() % n;
                if(x >= 0 && x < n && !randoms.contains(x)) {
                    randoms.add(x);
                    ++i;
                }
            }
            card1 = cards.get(randoms.get(0));
            card2 = cards.get(randoms.get(1));
            card3 = cards.get(randoms.get(2));
            card4 = cards.get(randoms.get(3));
        }
        System.out.println("cardGetByTag");

        model.addAttribute("isTrue1", true);
        model.addAttribute("isTrue2", true);
        model.addAttribute("isTrue3", true);
        model.addAttribute("isTrue4", true);
        model.addAttribute("isDone", false);

        session.setAttribute("card1", card1);
        session.setAttribute("card2", card2);
        session.setAttribute("card3", card3);
        session.setAttribute("card4", card4);

        model.addAttribute("card1", card1);
        model.addAttribute("card2", card2);
        model.addAttribute("card3", card3);
        model.addAttribute("card4", card4);
        return "learn";
    }

    @GetMapping("/profile/learn/exit")
    public String exit(){
        return "redirect:/profile";
    }
}
