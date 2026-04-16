package es.grupo3.practica25_26.dto;

public record UserCreateDTO(
        String userName,
        String surname,
        String address,
        String email,
        String password) {
}
