
package es.grupo3.practica25_26.restcontroller;

import java.util.Collection;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.grupo3.practica25_26.dto.ProductBasicDTO;
import es.grupo3.practica25_26.mapper.ProductBasicMapper;
import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.service.ProductService;

@RestController
@RequestMapping("/api/v1/products")
public class ProductRestController {

    @Autowired
    private ProductBasicMapper mapper;

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public Collection<ProductBasicDTO> getAllProducts() {
        return mapper.toDTOs(productService.findAll());
    }

    @GetMapping("/{id}")
    public ProductBasicDTO getProductById(@PathVariable long id) {
        Product product = productService.findById(id);
        if (product == null) {
            throw new NoSuchElementException("Product not found");
        }
        return mapper.toDTO(product);
    }

}
