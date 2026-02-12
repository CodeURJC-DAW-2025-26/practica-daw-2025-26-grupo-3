package es.grupo3.practica25_26.model;

import java.time.LocalDateTime;

public class Review {
    private String client;
    private String title;
    private String description;
    private LocalDateTime date;

    public Review() {
    }

    public Review(String client, String title, String description, LocalDateTime date) {
        this.client = client;
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
