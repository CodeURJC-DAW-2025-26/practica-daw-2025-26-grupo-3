package es.grupo3.practica25_26.model;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import es.grupo3.practica25_26.model.Order;

@Entity
@Table(name = "orders") // Crucial: 'order' is a reserved word in SQL, so we rename the table
public class Order {

    // Primary key for the database
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderID;

    // A list of items contained in this order.
    // CascadeType.ALL ensures operations on the order apply to its items.
    // orphanRemoval = true removes items from the DB if they are removed from this
    // list.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    // The user who placed the order.
    // FetchType.LAZY optimizes performance by loading the user only when requested.
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String date;

    // Represents the current status of the order: 0 = delivered, 1 = pending, 2 =
    // revise payment
    private int state;

    private String stateText;

    // Total calculated price of the order
    private double totalPrice;

    // Default no-args constructor required by JPA
    public Order() {
    }

    // Constructor to initialize an order with a user, date, and state
    public Order(User user, String date, int state) {
        this.user = user;
        this.date = date;
        this.state = state;
    }

    // --- Getters and Setters ---

    public Long getOrderID() {
        return orderID;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    // Sets the order items and automatically recalculates the total price to
    // maintain consistency
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        calculateTotalPrice();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateText() {
        return stateText;
    }

    public void setStateText(String stateText) {
        this.stateText = stateText;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    // Recalculates the total price of the order by iterating through the order
    // items
    // and summing their subtotals (product price * item quantity)
    public void calculateTotalPrice() {
        double total = 0.0;
        for (OrderItem item : orderItems) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        this.setTotalPrice(total);
    }
}