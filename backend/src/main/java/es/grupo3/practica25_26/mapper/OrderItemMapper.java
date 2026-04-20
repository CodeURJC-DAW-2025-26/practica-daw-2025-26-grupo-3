package es.grupo3.practica25_26.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import es.grupo3.practica25_26.dto.OrderItemDTO;
import es.grupo3.practica25_26.model.OrderItem;

@Mapper(componentModel = "spring", uses = { ProductBasicMapper.class }, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderItemMapper {

    // Entity to DTO
    public abstract OrderItemDTO toDTO(OrderItem orderItem);

    // Collections to DTOs
    public abstract List<OrderItemDTO> toDTOs(Collection<OrderItem> orderItems);

    // DTO to Entity
    public abstract OrderItem toDomain(OrderItemDTO orderItemDTO);
}
