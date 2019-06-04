package com.project.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Message {

    @Id
    @GeneratedValue
    private long id;

    private String message;

    @OneToOne
    @JoinColumn
    private User user;

    @ManyToOne
    @JoinColumn(name = "conversationId")
    private Conversation conversation;

    public Message() {
    }

    public Message(String message, User user, Conversation conv) {
        this.message = message;
        this.user = user;
        this.conversation = conv;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }


}
