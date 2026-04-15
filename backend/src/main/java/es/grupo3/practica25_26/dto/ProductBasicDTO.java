package es.grupo3.practica25_26.dto;

public record ProductBasicDTO(
        Long id,
        String productName,
        Double price,
        Integer state,
        String description,
        Double averageRating,
        Integer reviewCount,
        Long imageId) {
}
