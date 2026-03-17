
package es.grupo3.practica25_26.restcontroller;

import java.net.URI;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.grupo3.practica25_26.dto.ProductBasicDTO;
import es.grupo3.practica25_26.dto.ProductDTO;
import es.grupo3.practica25_26.mapper.ProductMapper;
import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.service.ProductService;
//import es.grupo3.practica25_26.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/products")
public class ProductRestController {

    @Autowired
    private ProductMapper mapper;

    @Autowired
    private ProductService productService;
    /* 
    @Autowired
    private UserService userService;
    */
    @GetMapping("/")
    public Collection<ProductBasicDTO> getAllProducts() {
        return mapper.toDTOs(productService.findAll());
    }

    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable long id) {
        Product product = productService.findById(id);
        if (product == null) {
            throw new NoSuchElementException("Product not found");
        }
        return mapper.toDTO(product);
    }

    @PostMapping("/")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductBasicDTO productBasicDTO) {

        
        Product product = mapper.toDomain(productBasicDTO);
        
        //SECURITY
        /* 
        String currentUsername = request.getUserPrincipal().getName();
        User seller = userService.findByEmailOrUsername(currentUsername); // Usa tu método real
        product.setSeller(seller);
        */

        //save the product in the ddbb
        productService.save(product);


        //create the URI for the Location header
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();

            //return the product transformed to the DTO
        return ResponseEntity.created(location).body(mapper.toDTO(product));
    }
    
}