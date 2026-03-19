package es.grupo3.practica25_26.mapper;

import org.mapstruct.Mapper;

import es.grupo3.practica25_26.dto.ImageDTO;
import es.grupo3.practica25_26.model.Image;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    ImageDTO toDTO(Image image);
    
}
