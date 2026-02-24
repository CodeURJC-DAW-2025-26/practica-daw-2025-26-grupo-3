package es.grupo3.practica25_26.model;

import java.util.Map;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyJoinColumn;

@Entity
public class ShoppingCart {

    // Unidirectional 1:N relationship

    @ElementCollection
    @CollectionTable(name = "shopping_cart_products", joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyJoinColumn(name = "product_id")
    @Column(name = "quantity")
    Map<Product, Long> products; // Product map saves (product, product nums) tuples.

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    public ShoppingCart() {

    }

    public ShoppingCart(Map<Product, Long> products) {
        this.products = products;
    }

    public Map<Product, Long> getProducts() {
        return products;
    }

    public void setProducts(Map<Product, Long> products) {
        this.products = products;
    }
}
