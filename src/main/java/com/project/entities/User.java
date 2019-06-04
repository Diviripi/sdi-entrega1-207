package com.project.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue
    private long id;

    private String name;
    private String lastName;
    private String password;
    @Transient // propiedad que no se almacena e la tabla.
    private String passwordConfirm;
    @Column(unique = true)
    private String email;
    private String role;
    private double money;



    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Product> products;


    public User(String email,String name, String lastName) {

        super();
        this.email=email;
        this.name = name;
        this.lastName = lastName;
    }

    public User() {
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getFullName() {
        return this.name + " " + this.lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public String getPasswordConfirm() {

        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {

        this.passwordConfirm = passwordConfirm;
    }

    public String getRole() {

        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
