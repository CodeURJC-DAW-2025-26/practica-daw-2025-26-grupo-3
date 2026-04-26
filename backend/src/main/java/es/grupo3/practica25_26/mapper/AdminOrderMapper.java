package es.grupo3.practica25_26.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import es.grupo3.practica25_26.dto.AdminOrderDataDTO;
import es.grupo3.practica25_26.model.Order;

@Mapper(componentModel = "spring", uses = { UserBasicMapper.class }, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminOrderMapper {

    // Entity to DTO
    public abstract AdminOrderDataDTO toDTO(Order order);

    // Collections to DTOs (¡Cuidado aquí! Tiene que ser AdminOrderDataDTO)
    public abstract List<AdminOrderDataDTO> toDTOs(Collection<Order> orders);
}