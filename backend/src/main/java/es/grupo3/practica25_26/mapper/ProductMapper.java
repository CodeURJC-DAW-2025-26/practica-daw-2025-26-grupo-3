package es.grupo3.practica25_26.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import es.grupo3.practica25_26.dto.ProductBasicDTO;
import es.grupo3.practica25_26.dto.ProductDTO;
import es.grupo3.practica25_26.model.Product;

import es.grupo3.practica25_26.dto.ReviewBasicDTO;
import es.grupo3.practica25_26.model.Review;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { UserBasicMapper.class })
public interface ProductMapper {

    // GET /api/v1/products/{id}
    @Mapping(target = "reviews", source = "reviews")
    ProductDTO toDTO(Product product);

    // GET /api/v1/products/
    List<ProductBasicDTO> toDTOs(Collection<Product> products);

    // POST y PUT
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "seller", ignore = true)
    Product toDomain(ProductBasicDTO productBasicDTO);

    ReviewBasicDTO toReviewBasicDTO(Review review);

}
