package es.grupo3.practica25_26.dto;

public record UserBasicDTO(
                long id,
                String userName,
                String surname,
                String address,
                String email,
                boolean state,
                int favouriteState) {
}