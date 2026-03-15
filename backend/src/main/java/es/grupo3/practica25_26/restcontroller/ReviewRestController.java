package es.grupo3.practica25_26.restcontroller;

import java.net.URI;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.grupo3.practica25_26.dto.ReviewPostDTO;
import es.grupo3.practica25_26.dto.ReviewDTO;
import es.grupo3.practica25_26.mapper.ReviewMapper;
import es.grupo3.practica25_26.mapper.ReviewPostMapper;
import es.grupo3.practica25_26.model.Review;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.service.ProductService;
import es.grupo3.practica25_26.service.ReviewService;
import es.grupo3.practica25_26.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/v1/products")
public class ReviewRestController {

    @Autowired
    private ReviewMapper mapper;

    @Autowired
    private ReviewPostMapper postMapper;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping("/all/reviews/")
    public Collection<ReviewDTO> getAllReviews() {
        return mapper.toDTOs(reviewService.findAllReviews());
    }

    @GetMapping("/all/reviews/{id}")
    public ReviewDTO getReviewById(@PathVariable long id) {
        return mapper.toDTO(reviewService.findReviewById(id));
    }

    @DeleteMapping("/{productID}/reviews/{reviewID}")
    public ResponseEntity<ReviewDTO> deleteReviewById(@PathVariable long productID, @PathVariable long reviewID,
            HttpServletRequest request) {
        String currentUserEmail = request.getUserPrincipal().getName();
        User currentUser = userService.findUserByEmail(currentUserEmail);

        ResponseEntity<ReviewDTO> response = ResponseEntity
                .ok(mapper.toDTO(reviewService.deleteReview(reviewID, productID, currentUserEmail,
                        currentUser.getRoles().indexOf("ADMIN") != -1)));

        return response;
    }

    @PostMapping("/{productId}/reviews/")
    public ResponseEntity<ReviewDTO> createReview(@PathVariable long productId, @RequestBody ReviewPostDTO reviewDTO,
            HttpServletRequest request) {
        Review newReview = postMapper.toDomain(reviewDTO);

        // Find existing user by email
        String email = request.getUserPrincipal().getName();
        if (email != null) {
            User existingUser = userService.findUserByEmail(email);
            if (existingUser != null) {
                newReview.setUser(existingUser);
            } else {
                throw new NoSuchElementException("User with email " + email + " not found.");
            }
        } else {
            throw new IllegalArgumentException("User email must be provided.");
        }

        Product product = productService.findById(productId);
        Product reviewProduct;

        if (product != null) {
            reviewProduct = product;
        } else {
            throw new NoSuchElementException("Product not found.");
        }

        reviewService.saveReview(reviewProduct, newReview);

        URI location = fromCurrentRequest().path("/{reviewId}").buildAndExpand(newReview.getId()).toUri();
        return ResponseEntity.created(location).body(mapper.toDTO(newReview));
    }

    @PutMapping("/{productId}/reviews/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable long productId, @PathVariable long reviewId,
            @RequestBody ReviewPostDTO updatedReviewDTO, HttpServletRequest request) {
        Review updatedReview = postMapper.toDomain(updatedReviewDTO);

        String currentUserEmail = request.getUserPrincipal().getName();
        User currentUser = userService.findUserByEmail(currentUserEmail);

        updatedReview.setId(reviewId);
        updatedReview.setUser(currentUser);

        reviewService.updateReview(updatedReview, currentUserEmail);
        return ResponseEntity.ok(mapper.toDTO(updatedReview));
    }
}