package es.grupo3.practica25_26.dto;

import java.util.List;

public record ProductBasicDTO(
                Long id,
                String productName,
                String description,
                double price,
                String stateName,
                String stateClass,
                double averageRating,
                List<Boolean> stars,
                int reviewCount,
                UserBasicDTO seller) {
}
