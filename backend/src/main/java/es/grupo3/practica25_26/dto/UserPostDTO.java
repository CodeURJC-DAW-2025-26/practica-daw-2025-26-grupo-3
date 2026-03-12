package es.grupo3.practica25_26.dto;

public record UserPostDTO(
                String userName,
                String surname,
                String address,
                String email,
                boolean state,
                String password) {
}