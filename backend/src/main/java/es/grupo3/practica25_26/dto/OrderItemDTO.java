package es.grupo3.practica25_26.dto;

public record OrderItemDTO(
                long id,
                ProductBasicDTO product,
                int quantity) {

}
