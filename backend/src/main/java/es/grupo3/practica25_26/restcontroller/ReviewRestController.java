package es.grupo3.practica25_26.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.grupo3.practica25_26.dto.ReviewDTO;
import es.grupo3.practica25_26.mapper.ReviewMapper;
import es.grupo3.practica25_26.service.ProductService;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewRestController {

    @Autowired
    private ReviewMapper mapper;

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public ReviewDTO getReviewById(@PathVariable long id) {
        return mapper.toDTO(productService.findReviewById(id));
    }
}
