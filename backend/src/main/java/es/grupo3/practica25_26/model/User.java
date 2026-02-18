package es.grupo3.practica25_26.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.*;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id; // Primary Key

    private String userName;
    private String surname;
    private String address;

    // identifier for login, must be unique
    @Column(unique = true)
    private String email;

    private String password;

    // Stores a list of roles (Strings) in a separate table and loads them
    // immediately with the User because the default case for @ElementCollection is
    // LAZY, but we want to have the roles available as soon as we load the user.
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Product> products;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) // This makes de image to be
                                                                                  // persistent when created.
    private Image image;

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public User() {

    }

    public Image getImage() {
        return image;
    }

    public User(String userName, String surname, String address, String email, String password) {
        this.userName = userName;
        this.surname = surname;
        this.address = address;
        this.email = email;
        this.password = password;
        this.roles = new ArrayList<>();
        this.roles.add("USER");
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    // We store the encoded password in the database, so this getter returns the
    // encoded version.
    public String getEncodedPassword() {
        return password;
    }

    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    // Getter for roles
    public List<String> getRoles() {
        return roles;
    }

    // Setter for roles
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    // Añadir relación con productos y pedidos

}
