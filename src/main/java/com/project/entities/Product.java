package com.project.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Product {


    @Id
    @GeneratedValue
    private long id;


    private String title;
    private String description;
    private double price;
    private Date date;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private boolean highlighted;


    private boolean sold;
    @OneToOne
    @JoinColumn(name = "buyer_id")
    private User buyer;


    public Product() {
    }

    public Product(String title, String description, double price) {
        super();
        this.title = title;
        this.description = description;
        setPrice(price);

    }

    public Product(String title, String description, double price, User user) {
        super();
        this.title = title;
        this.description = description;
        setPrice(price);
        this.user = user;
        this.highlighted=false;
    }

    public Product(String title, String description, double price, User user, boolean highlighted) {
        super();
        this.title = title;
        this.description = description;
        setPrice(price);
        this.user = user;
        this.highlighted = highlighted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        int temp = (int) (price * 100);
        double val = temp / 100.00;
        this.price = val;
    }

    public double getPrice() {

        return price;
    }

    public String getFormatedPrice() {
        String strDouble = String.format("%.2f", this.price);
        return strDouble;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", date=" + date +
                ", user=" + user +
                '}';
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }
}
