package es.grupo3.practica25_26.model;

import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;

@Entity
public class ShoppingCart {

    // Unidirectional 1:N relationship
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "shopping_cart_id")
    List<CartItem> cartItems = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    public ShoppingCart() {

    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public long getId() {
        return id;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void setId(long id) {
        this.id = id;
    }

}
