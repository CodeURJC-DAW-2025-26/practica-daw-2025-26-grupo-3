package es.grupo3.practica25_26.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items") // custom database name
public class OrderItem {

    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Foreign key to order table
    @ManyToOne // Many products can be in one order
    @JoinColumn(name = "order_id")
    private Order order;

    // Foreign key to product table
    @ManyToOne // Many order items can refer to one product
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity; // num of products in the order
    private double unitPrice; // Price of one unit of the product at the time of the order was created
    private double subtotal;

    public OrderItem() {
    }

    public OrderItem(Order order, Product product, int quantity, double unitPrice, double subtotal) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.subtotal = this.quantity * this.unitPrice; // update total price of the order, num of items * price of the
                                                        // item
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        this.subtotal = this.quantity * this.unitPrice; // update total price of the order, num of items * price of the
                                                        // item

    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

}
