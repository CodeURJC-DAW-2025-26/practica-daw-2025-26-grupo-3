package es.grupo3.practica25_26.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import es.grupo3.practica25_26.dto.UserBasicDTO;
import es.grupo3.practica25_26.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserBasicMapper {
    @Mapping(source = "image.id", target = "imageId")
    UserBasicDTO toDTO(User user);

    List<UserBasicDTO> toDTOs(Collection<User> users);

    User toDomain(UserBasicDTO userBasicDTO);

}