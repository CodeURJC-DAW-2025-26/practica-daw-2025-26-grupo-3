
package es.grupo3.practica25_26.controller.restcontroller;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.grupo3.practica25_26.dto.ImageDTO;
import es.grupo3.practica25_26.dto.ProductBasicDTO;
import es.grupo3.practica25_26.dto.ProductDTO;
import es.grupo3.practica25_26.mapper.ImageMapper;
import es.grupo3.practica25_26.mapper.ProductBasicMapper;
import es.grupo3.practica25_26.mapper.ProductMapper;
import es.grupo3.practica25_26.model.Image;
import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.service.ImageService;
import es.grupo3.practica25_26.service.ProductService;
import es.grupo3.practica25_26.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

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

    @Autowired
    private ImageService imageService;

    @Autowired
    private ImageMapper imageMapper;

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
    @PutMapping("/{id}")
    public ProductBasicDTO updateProduct(@PathVariable long id, @RequestBody ProductBasicDTO productBasicDTO,
            HttpServletRequest request) throws IOException {

        Product editedproduct = basicProductMapper.toDomain(productBasicDTO);
        String loggedInEmail = request.getUserPrincipal().getName();
        boolean isAdmin = request.isUserInRole("ADMIN");

        Product updatedProduct = productService.updateProduct(id, editedproduct, null, null, loggedInEmail, isAdmin);

        return basicProductMapper.toDTO(updatedProduct);
    }

    // Delete a product (delete)
    @DeleteMapping("/{id}")
    public ProductBasicDTO deleteProduct(@PathVariable long id, HttpServletRequest request) {

        return basicProductMapper.toDTO(
                productService.deleteProduct(id, request.getUserPrincipal().getName(), request.isUserInRole("ADMIN")));
    }

    // Create an image for the product

    @PostMapping("/{id}/images/")
    public ResponseEntity<ImageDTO> createProductImage(@PathVariable long id, @RequestParam MultipartFile imageFile,
            HttpServletRequest request) throws IOException {

        if (imageFile.isEmpty()) {
            throw new IllegalArgumentException("Image file cannot be empty");
        }

        String loggedInEmail = request.getUserPrincipal().getName();
        boolean isAdmin = request.isUserInRole("ADMIN");

        Image image = imageService.createImage(imageFile.getInputStream());
        productService.addImageToProduct(id, image, loggedInEmail, isAdmin);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/{imageId}/media")
                .buildAndExpand(image.getId())
                .toUri();

        return ResponseEntity.created(location).body(imageMapper.toDTO(image));
    }

    // Delete an image from the product

    @DeleteMapping("/{productId}/images/{imageId}")
    public ImageDTO deleteProductImage(@PathVariable long productId, @PathVariable long imageId,
            HttpServletRequest request) {

        String loggedInEmail = request.getUserPrincipal().getName();
        boolean isAdmin = request.isUserInRole("ADMIN");

        Image image = imageService.getImage(imageId);
        productService.removeImageFromProduct(productId, imageId, loggedInEmail, isAdmin);

        return imageMapper.toDTO(image);
    }

}