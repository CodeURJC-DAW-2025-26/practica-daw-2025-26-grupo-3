package es.grupo3.practica25_26.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

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
    private Integer state; // 0 New product, 1 reconditioned product, 2 Second hand product

    @Column(length = 1000) // Set max length of description to 1000 characters (The default is 255)
    private String description;

    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Review> reviews;

    public Product() {
    }

    public Product(String productName, double price, Integer state, String description, User seller) {
        this.productName = productName;
        this.price = price;
        this.state = state;
        this.description = description;
        this.seller = seller;
        reviews = new ArrayList<>();
    }

    public Product(User seller, List<Image> images, String productName, double price, Integer state,
            String description) {
        this.seller = seller;
        this.images = images;
        this.productName = productName;
        this.price = price;
        this.state = state;
        this.description = description;
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

    public Integer getState() {
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

    public void setState(Integer state) {
        this.state = state;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Image getFirstImage() {

        if (images != null && !images.isEmpty()) { // If list of images not empty
            return images.get(0);
        }
        return null; // If empty return null

    }

    public boolean hasImage() {
        return images != null && !images.isEmpty();
    }

    public String getStateName() {
        if (this.state == null) {
            return "Estado desconocido";
        }

        switch (this.state) {
            case 0:
                return "Nuevo";
            case 1:
                return "Reacondicionado";
            case 2:
                return "Segunda Mano";
            default:
                return "Estado desconocido";
        }
    }

    // Method to get the CSS class for the state badge
    public String getStateClass() {
        if (this.state == null) {
            return "bg-secondary text-white";
        }

        switch (this.state) {
            case 0: // new
                return "bg-primary bg-opacity-10 text-primary"; // light blue
            case 1: // reconditioned
                return "bg-info bg-opacity-10 text-info"; // Blue
            case 2: // second hand
                return "bg-warning bg-opacity-10 text-warning"; // Yellow
            default:
                return "bg-secondary text-white";
        }
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    // Method to calculate the average rating of the product
    public double getAverageRating() {

        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }
        double sum = 0;
        for (Review review : reviews) {
            sum += review.getStars();
        }
        return sum / reviews.size(); // returns the average rating of the product
    }

    // Method to get a list of booleans representing the stars for the product
    public List<Boolean> getStars() {
        List<Boolean> stars = new ArrayList<>();
        int fullStars = (int) Math.round(getAverageRating());
        for (int i = 0; i < 5; i++) {
            // If the index is less than the number of full stars, add true (filled star),
            // otherwise add false (empty star)
            if (i < fullStars) {
                stars.add(true);
            } else {
                stars.add(false);
            }
        }
        return stars;
    }

    public int getReviewCount() {

        if (reviews == null) {
            return 0;
        } else {
            return reviews.size();
        }
    }

}
