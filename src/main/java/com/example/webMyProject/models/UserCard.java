package com.example.webMyProject.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Entity
@Table(name="user_card")
public class UserCard {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id")
    private long id;

    @NotNull
    @Column(name="user_id")
    private long userId;

    @NotNull
    @Column(name="card_id")
    private long cardId;

    @Column(name="add_date")
    private Date date;

    @NotNull
    @Column(name="is_learn")
    private boolean isLearn;

    public UserCard(){
    }

    public UserCard(long card_id, long userId, Date date, boolean isLearn) {
        this.cardId = card_id;
        this.userId = userId;
        this.date = date;
        this.isLearn = isLearn;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public boolean isLearn() {
        return isLearn;
    }

    public void setLearn(boolean learn) {
        isLearn = learn;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
