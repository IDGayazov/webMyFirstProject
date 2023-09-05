package com.example.webMyProject.models;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="card")
public class Card implements Serializable{

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name="image")
    @NotBlank(message="Please, fill the field!")
    @Length(max=255, message="Word too long")
    private String image;
    @Column(name="tat")
    @NotBlank(message="Please, fill the field!")
    @Length(max=255, message="Word too long")
    private String tat;
    @Column(name="rus")
    @Length(max=255, message="Word too long")
    @NotBlank(message="Please, fill the field!")
    private String rus;

    @Column(name="tag")
    @NotBlank(message="Please, fill the field!")
    private String tag;
    @Column(name="is_global")
    private boolean isGlobal;

    @ManyToMany
    @JoinTable (name="user_card",
            joinColumns=@JoinColumn (name="card_id"),
            inverseJoinColumns=@JoinColumn(name="user_id"))
    private List<User> users;

    public Card() {
    }

    public Card(String image, String tat, String rus, String tag, boolean isGlobal) {
        this.image = image;
        this.tat = tat;
        this.rus = rus;
        this.tag = tag;
        this.isGlobal = isGlobal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTat() {
        return tat;
    }

    public void setTat(String tat) {
        this.tat = tat;
    }

    public String getRus() {
        return rus;
    }

    public void setRus(String rus) {
        this.rus = rus;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal(boolean global) {
        isGlobal = global;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
