
package es.grupo3.practica25_26.restcontroller;

import java.net.URI;
import java.security.Principal;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.grupo3.practica25_26.dto.ProductBasicDTO;
import es.grupo3.practica25_26.dto.ProductDTO;
import es.grupo3.practica25_26.mapper.ProductBasicMapper;
import es.grupo3.practica25_26.mapper.ProductMapper;
import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.service.ProductService;
import es.grupo3.practica25_26.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/products")
public class ProductRestController {

    @Autowired
    private ProductBasicMapper basicProductMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    // get all products
    @GetMapping("/")
    public Collection<ProductBasicDTO> getAllProducts() {
        return basicProductMapper.toDTOs(productService.findAll());
    }

    // get specific product by id
    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable long id) {
        Product product = productService.findById(id);
        if (product == null) {
            throw new NoSuchElementException("Product not found");
        }
        return productMapper.toDTO(product);
    }

    // Create a new product
    @PostMapping("/")
    public ResponseEntity<ProductBasicDTO> createProduct(@RequestBody ProductBasicDTO productBasicDTO,
            HttpServletRequest request) {

        Product product = basicProductMapper.toDomain(productBasicDTO);

        // We get the current user from the request and set it as the seller of the
        // product
        String currentUseremail = request.getUserPrincipal().getName();
        User seller = userService.findUserByEmail(currentUseremail);
        product.setSeller(seller);

        // save the product in the ddbb
        productService.save(product);

        // create the URI for the Location header
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();

        // return the product transformed to the DTO
        return ResponseEntity.created(location).body(basicProductMapper.toDTO(product));
    }

    // Update a product (put)

    // Delete a product (delete)
    @DeleteMapping("/{id}")
    public ProductBasicDTO deleteProduct(@PathVariable long id, HttpServletRequest request) {
        request.isUserInRole("ADMIN");
        request.getUserPrincipal().getName();
        return basicProductMapper.toDTO(
                productService.deleteProduct(id, request.getUserPrincipal().getName(), request.isUserInRole("ADMIN")));
    }

}