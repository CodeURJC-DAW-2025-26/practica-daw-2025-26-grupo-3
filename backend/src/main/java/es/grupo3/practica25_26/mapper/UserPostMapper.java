package es.grupo3.practica25_26.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import es.grupo3.practica25_26.dto.UserPostDTO;
import es.grupo3.practica25_26.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserPostMapper {
    UserPostDTO toDTO(User user);

    List<UserPostDTO> toDTOs(Collection<User> users);

    User toDomain(UserPostDTO userPost);
}
