package es.grupo3.practica25_26.controller.restcontroller;

import es.grupo3.practica25_26.service.ProductService;
import java.net.URI;
import java.util.List;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import es.grupo3.practica25_26.dto.ReviewPostDTO;
import es.grupo3.practica25_26.dto.ReviewDTO;
import es.grupo3.practica25_26.mapper.ReviewMapper;
import es.grupo3.practica25_26.mapper.ReviewPostMapper;
import es.grupo3.practica25_26.model.Review;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.model.Error;
import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.service.ReviewService;
import es.grupo3.practica25_26.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/v1/products")
public class ReviewRestController {

        private final ProductService productService;

        @Autowired
        private ReviewMapper mapper;

        @Autowired
        private ReviewPostMapper postMapper;

        @Autowired
        private ReviewService reviewService;

        @Autowired
        private UserService userService;

        ReviewRestController(ProductService productService) {
                this.productService = productService;
        }

        @Operation(summary = "Get all reviews")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully")
        })
        @GetMapping("/all/reviews/")
        public Collection<ReviewDTO> getAllReviews() {
                return mapper.toDTOs(reviewService.findAllReviews());
        }

        @Operation(summary = "Get review by ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Review found"),
                        @ApiResponse(responseCode = "404", description = "Review not found")
        })
        @GetMapping("/all/reviews/{id}")
        public ReviewDTO getReviewById(@PathVariable long id) {
                return mapper.toDTO(reviewService.findReviewById(id));
        }

        @GetMapping("/{productId}/reviews")
        public Collection<ReviewDTO> getReviewsByProductId(@PathVariable long productId) {
                Product product = productService.findById(productId);
                List<Review> reviewList = product.getReviews();
                return mapper.toDTOs(reviewList);
        }

        @Operation(summary = "Delete review")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Review deleted successfully"),
                        @ApiResponse(responseCode = "404", description = "Review not found"),
                        @ApiResponse(responseCode = "403", description = "Forbidden (Not owner or admin)")
        })
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

        @Operation(summary = "Create review for a product")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Review created successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid review data"),
                        @ApiResponse(responseCode = "404", description = "Product not found")
        })
        @PostMapping("/{productId}/reviews/")
        public ResponseEntity<ReviewDTO> createReview(@PathVariable long productId,
                        @RequestBody ReviewPostDTO reviewDTO,
                        HttpServletRequest request) {
                Review newReview = postMapper.toDomain(reviewDTO);

                reviewService.createReviewApi(newReview, productId, request);

                URI location = fromCurrentRequest().path("/{reviewId}").buildAndExpand(newReview.getId()).toUri();
                return ResponseEntity.created(location).body(mapper.toDTO(newReview));
        }

        @Operation(summary = "Update a review")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Review updated successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid review data"),
                        @ApiResponse(responseCode = "404", description = "Review not found"),
                        @ApiResponse(responseCode = "403", description = "Forbidden (Not owner)")
        })
        @PutMapping("/{productId}/reviews/{reviewId}")
        public ResponseEntity<ReviewDTO> updateReview(@PathVariable long productId, @PathVariable long reviewId,
                        @RequestBody ReviewPostDTO updatedReviewDTO, HttpServletRequest request) {
                Review updatedReview = postMapper.toDomain(updatedReviewDTO);

                String currentUserEmail = request.getUserPrincipal().getName();
                User currentUser = userService.findUserByEmail(currentUserEmail);

                Error error = reviewService.reviewCheck(updatedReview.getTitle(), updatedReview.getBody(),
                                updatedReview.getStars());
                if (error != null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "There are errors in your review create request: " + error.getTitle() + " "
                                                        + error.getMessage());
                }

                updatedReview.setId(reviewId);
                updatedReview.setUser(currentUser);

                reviewService.updateReview(updatedReview, currentUserEmail);
                return ResponseEntity.ok(mapper.toDTO(updatedReview));
        }
}