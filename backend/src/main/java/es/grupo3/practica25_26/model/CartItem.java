package es.grupo3.practica25_26.model;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @ManyToOne
    Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    int quantity;
    int listIndex;
    String state;

    public CartItem() {

    }

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.listIndex = 0;
    }

    public CartItem(Product product, int quantity, int listIndex) {
        this.product = product;
        this.quantity = quantity;
        this.listIndex = listIndex;
    }

    public String getState() {
        return state;
    }

    public long getId() {
        return id;
    }

    public int getListIndex() {
        return listIndex;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setListIndex(int listIndex) {
        this.listIndex = listIndex;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
