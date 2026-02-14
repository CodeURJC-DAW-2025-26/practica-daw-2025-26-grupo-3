package es.grupo3.practica25_26.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
public class Product {

    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    // Foreign key to user table
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User seller;

    private String productName;
    private double price;
    private int state; // 0 New product, 1 reconditioned product, 2 Second hand product
    private String description;

    public Product() {
    }

    public Product(String productName, double price, int state, String description, User seller) {
        this.productName = productName;
        this.price = price;
        this.state = state;
        this.description = description;
        this.seller = seller;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public int getState() {
        return state;
    }

    public String getDescription() {
        return description;
    }

    public User getSeller() {
        return seller;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

}
