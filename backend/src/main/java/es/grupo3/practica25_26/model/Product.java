package es.grupo3.practica25_26.model;

public class Product {

    private String productName;
    private double price;
    private int state; // 0 New product, 1 reconditioned product, 2 Second hand product
    private String description;

    public Product() {
    }

    public Product(String productName, double price, int state, String description) {
        this.productName = productName;
        this.price = price;
        this.state = state;
        this.description = description;
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

}
