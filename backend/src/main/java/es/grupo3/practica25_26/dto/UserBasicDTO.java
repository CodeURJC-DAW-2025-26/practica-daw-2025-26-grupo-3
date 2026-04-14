package es.grupo3.practica25_26.dto;

import java.util.List;

public record UserBasicDTO(
                long id,
                String userName,
                String surname,
                String address,
                String email,
                boolean state,
                int favouriteState,
                Long imageId,
                List<String> roles) {
}