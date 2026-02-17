package es.grupo3.practica25_26.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id; // Primary Key

    private String userName;
    private String surname;
    private String address;

    @Column(unique = true)
    private String email;

    private String password;

    // it save role in the BD as text, for example, "USER" or "ADMIN", instead of
    // using the ordinal value of the enum
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Product> products;

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public User() {

    }

    public User(String userName, String surname, String address, String email, String password) {
        this.userName = userName;
        this.surname = surname;
        this.address = address;
        this.email = email;
        this.password = password;
        this.role = Role.USER;
    }

    public User(String userName, String surname, String address, String email, String password, Role role) {
        this.userName = userName;
        this.surname = surname;
        this.address = address;
        this.email = email;
        this.password = password;
        this.role = role;
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

    public String getPassword() {
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

    // it is used to retrieve the role of the user, for example, to check
    // permissions or to display the role in the user profile
    public Role getRole() {
        return role;
    }

    // it is used to assign a role to the user, for example, when creating a new
    // user or when changing the role of an existing user
    public void setRole(Role role) {
        this.role = role;
    }

    // Añadir relación con productos y pedidos

}
