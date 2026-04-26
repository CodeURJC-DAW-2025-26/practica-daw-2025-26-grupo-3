package es.grupo3.practica25_26.dto;

public record AdminOrderDataDTO(
                long orderID,
                String date,
                int state,
                double totalPrice,
                UserBasicDTO user) {

}
