package es.grupo3.practica25_26.dto;

public record ProductBasicDTO(
                Long id,
                String productName,
                double price,
                Integer state,
                String description,
                double averageRating,
                int reviewCount) {
}
