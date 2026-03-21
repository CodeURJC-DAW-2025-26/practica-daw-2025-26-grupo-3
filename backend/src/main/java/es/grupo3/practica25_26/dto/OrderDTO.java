package es.grupo3.practica25_26.dto;

import java.util.List;

public record OrderDTO(
        long orderID,
        List<OrderItemDTO> orderItems,
        double totalPrice) {
}
