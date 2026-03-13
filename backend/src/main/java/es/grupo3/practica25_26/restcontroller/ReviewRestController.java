package es.grupo3.practica25_26.restcontroller;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.grupo3.practica25_26.dto.ReviewPostDTO;
import es.grupo3.practica25_26.dto.ReviewDTO;
import es.grupo3.practica25_26.mapper.ReviewMapper;
import es.grupo3.practica25_26.model.Review;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.service.ProductService;
import es.grupo3.practica25_26.service.ReviewService;
import es.grupo3.practica25_26.service.UserService;

@RestController
@RequestMapping("/api/v1/product")
public class ReviewRestController {

    @Autowired
    private ReviewMapper mapper;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping("/review")
    public Collection<ReviewDTO> getAllReviews() {
        return mapper.toDTOs(reviewService.findAllReviews());
    }

    @GetMapping("/review/{id}")
    public ReviewDTO getReviewById(@PathVariable long id) {
        return mapper.toDTO(reviewService.findReviewById(id));
    }

    @DeleteMapping("/{productID}/review/{reviewID}")
    public ReviewDTO deleteReviewById(@PathVariable long productID, @PathVariable long reviewID) {
        return mapper.toDTO(reviewService.deleteReviewNoAuth(reviewID, productID));
    }

    @PostMapping("/{productId}/review")
    public ReviewDTO createReview(@PathVariable long productId, @RequestBody ReviewPostDTO reviewDTO) {

        Review newReview = new Review();
        newReview.setTitle(reviewDTO.title());
        newReview.setBody(reviewDTO.body());
        newReview.setStars(reviewDTO.stars());
        newReview.setDate(reviewDTO.date());

        // Find existing user by email
        String email = reviewDTO.userEmail();
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

        Optional<Product> op = productService.findById(productId);
        Product reviewProduct;

        if (op.isPresent()) {
            reviewProduct = op.get();
        } else {
            throw new NoSuchElementException("Product not found.");
        }

        reviewService.saveReview(reviewProduct, newReview);
        return mapper.toDTO(newReview);
    }
}
