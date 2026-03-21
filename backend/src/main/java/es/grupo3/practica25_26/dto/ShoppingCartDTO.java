package es.grupo3.practica25_26.dto;

import java.util.List;

// DTO for ShoppingCart entity, we return the id and the list of cart items with their ids and quantity
public record ShoppingCartDTO(
        long id,
        List<CartItemDTO> cartItems) {
}
