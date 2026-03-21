package es.grupo3.practica25_26.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import es.grupo3.practica25_26.dto.OrderDTO;
import es.grupo3.practica25_26.model.Order;

@Mapper(componentModel = "spring", uses = { OrderItemMapper.class }, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    // Entity to DTO
    public abstract OrderDTO toDTO(Order order);

    // Collections to DTOs
    public abstract List<OrderDTO> toDTOs(Collection<Order> orders);

    // DTO to Entity
    public abstract Order toDomain(OrderDTO orderDTO);
}
