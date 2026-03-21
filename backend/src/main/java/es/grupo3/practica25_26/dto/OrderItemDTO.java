package es.grupo3.practica25_26.dto;

public record OrderItemDTO(
        long id,
        long productId,
        String productName,
        int quantity) {

}
