package es.grupo3.practica25_26.dto;

public record ReviewPostDTO(
                String userEmail,
                String title,
                String body,
                String date,
                int stars) {
}