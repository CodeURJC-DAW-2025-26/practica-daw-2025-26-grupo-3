package es.grupo3.practica25_26.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;

    @Transient
    private boolean canModifyReview;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    private String title;
    private String body;
    private String date;
    private long imageId;

    public long getId() {
        return id;
    }

    public int getStars() {
        return stars;
    }

    public long getImageId() {
        return imageId;
    }

    private int stars;

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public Review() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public Review(User user, String title, String body, String date, int stars, long imageId) {
        this.user = user;
        this.title = title;
        this.body = body;
        this.date = date;
        this.stars = stars;
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public String getDate() {
        return date;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isCanModifyReview() {
        return canModifyReview;
    }

    public void setCanModifyReview(boolean canModifyReview) {
        this.canModifyReview = canModifyReview;
    }

}
