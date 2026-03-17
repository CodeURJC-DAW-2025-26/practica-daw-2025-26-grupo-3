package es.grupo3.practica25_26.dto;

import java.util.List;

import es.grupo3.practica25_26.model.Image;

public record UserBasicDTO(
                long id,
                String userName,
                String surname,
                String address,
                String email,
                boolean state,
                int favouriteState,
                Image image,
                List<String> roles) {
}