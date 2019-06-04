package com.project.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Conversation {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "user1_id")
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user2_id")
    private User user2;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    private Set<Message> messages;

    public Conversation() {
    }

    public Conversation(User user1, User user2, Product product) {
        super();
        this.user1 = user1;
        this.user2 = user2;
        this.product = product;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user) {
        this.user1 = user;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user) {
        this.user2 = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
