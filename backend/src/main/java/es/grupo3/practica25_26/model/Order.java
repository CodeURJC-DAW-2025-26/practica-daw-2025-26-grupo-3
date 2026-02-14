package es.grupo3.practica25_26.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderID;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    private String client;
    private LocalDateTime date;
    private int state; // 0 delivered, 1 pending, 2 revise payment
    private double totalPrice; // Total price of the order

    public Order() {
    }

    public Order(String client, LocalDateTime date, int state) {
        this.client = client;
        this.date = date;
        this.state = state;
    }

    public Long getOrderID() {
        return orderID;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        calculateTotalPrice();
    }

    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
        calculateTotalPrice();
    }

    public void removeOrderItem(OrderItem item) {
        orderItems.remove(item);
        item.setOrder(null);
        calculateTotalPrice();
    }

    // Recalculate total price of the order by iterating through the order items and
    // summing their subtotals
    public void calculateTotalPrice() {
        double total = 0.0;
        for (OrderItem item : orderItems) {
            total += item.getSubtotal();
        }
        this.totalPrice = total;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

}
