package es.grupo3.practica25_26.dto;

public record CartItemDTO(
        long id,
        long productId,
        String productName,
        int quantity) {
}
