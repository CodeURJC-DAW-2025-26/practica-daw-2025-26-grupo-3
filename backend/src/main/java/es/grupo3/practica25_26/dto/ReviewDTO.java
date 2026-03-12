package es.grupo3.practica25_26.dto;

public record ReviewDTO(
        long id,
        UserBasicDTO user,
        String title,
        String body,
        String date,
        int stars) {
}
