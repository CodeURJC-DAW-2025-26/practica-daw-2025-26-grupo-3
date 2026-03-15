package es.grupo3.practica25_26.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import es.grupo3.practica25_26.dto.ReviewDTO;
import es.grupo3.practica25_26.dto.ReviewPostDTO;
import es.grupo3.practica25_26.model.Review;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewPostMapper {
    ReviewDTO toDTO(Review review);

    List<ReviewPostDTO> toDTOs(Collection<Review> reviews);

    Review toDomain(ReviewPostDTO reviewDTO);

}