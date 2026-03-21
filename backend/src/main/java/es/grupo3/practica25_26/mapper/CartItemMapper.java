package es.grupo3.practica25_26.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import es.grupo3.practica25_26.dto.CartItemDTO;
import es.grupo3.practica25_26.model.CartItem;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartItemMapper {
    // Entity to DTO
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.productName", target = "productName")
    CartItemDTO toDTO(CartItem cartItem);

    // Collections to DTOs
    List<CartItemDTO> toDTOs(Collection<CartItem> cartItems);

    // DTO to Entity
    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "productName", target = "product.productName")
    CartItem toDomain(CartItemDTO cartItemDTO);

}
