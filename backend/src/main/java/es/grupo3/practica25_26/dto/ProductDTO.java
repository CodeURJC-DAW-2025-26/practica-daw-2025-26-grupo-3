package es.grupo3.practica25_26.dto;

import java.util.List;

public record ProductDTO(
        Long id,
        String productName,
        double price,
        Integer state,
        String description,
        double averageRating,
        int reviewCount,
        UserBasicDTO seller, 
        List<ImageDTO> images,
        List<ReviewBasicDTO> reviews
) {
    
}