package es.grupo3.practica25_26.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import es.grupo3.practica25_26.dto.ShoppingCartDTO;
import es.grupo3.practica25_26.model.ShoppingCart;

// we use uses to specify that we want to use the CartItemMapper to map the cart items of the shopping cart, and we ignore unmapped target properties because we don't want to map the user property of the shopping cart
@Mapper(componentModel = "spring", uses = { CartItemMapper.class }, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ShoppingCartMapper {
    // Entity to DTO
    ShoppingCartDTO toDTO(ShoppingCart newOrder);

    // Collection to DTOs
    List<ShoppingCartDTO> toDTOs(Collection<ShoppingCart> shoppingCarts);

    // DTO to Entity
    ShoppingCart toDomain(ShoppingCartDTO shoppingCartDTO);
}
