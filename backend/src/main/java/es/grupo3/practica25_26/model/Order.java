package es.grupo3.practica25_26.model;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private String orderID;
    private String client;
    private LocalDateTime date;
    private List<Product> productList;
    private int state; // 0 delivered, 1 pending, 2 revise payment
    private double totalPrice;

    public Order() {
    }

    public Order(String orderID, String client, LocalDateTime date, List<Product> productList, int state,
            double totalPrice) {
        this.orderID = orderID;
        this.client = client;
        this.date = date;
        this.productList = productList;
        this.state = state;
        this.totalPrice = totalPrice;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
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

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
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
