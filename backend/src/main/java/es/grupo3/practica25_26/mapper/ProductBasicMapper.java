package es.grupo3.practica25_26.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import es.grupo3.practica25_26.dto.ProductBasicDTO;
import es.grupo3.practica25_26.model.Product;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductBasicMapper {

    ProductBasicDTO toDTO(Product product);

    List<ProductBasicDTO> toDTOs(Collection<Product> products);

    Product toDomain(ProductBasicDTO productBasicDTO);

}
