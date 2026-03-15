package es.grupo3.practica25_26.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.model.Error;
import es.grupo3.practica25_26.model.Review;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.repository.ReviewRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductService productService;

    /**
     * Validates review fields for creation. Returns Error if invalid, null if OK.
     */
    public Error reviewCreateCheck(String title, String body, Integer stars) {
        if (title == null || title.trim().isEmpty()) {
            return new Error("¡Título vacío!", "El título de la reseña no puede estar vacío.");
        }
        if (title.length() < 3 || title.length() > 100) {
            return new Error("¡Longitud de título incorrecta!",
                    "El título debe tener entre 3 y 100 caracteres. Has introducido " + title.length()
                            + " caracteres.");
        }
        if (body == null || body.trim().isEmpty()) {
            return new Error("¡Cuerpo vacío!", "El cuerpo de la reseña no puede estar vacío.");
        }
        if (body.length() < 10 || body.length() > 1000) {
            return new Error("¡Longitud de cuerpo incorrecta!",
                    "El cuerpo debe tener entre 10 y 1000 caracteres. Has introducido " + body.length()
                            + " caracteres.");
        }
        if (stars == null) {
            return new Error("¡Valoración vacía!", "Debes seleccionar una cantidad de estrellas.");
        }
        if (stars < 1 || stars > 5) {
            return new Error("¡Cantidad de estrellas inválida!",
                    "La cantidad de estrellas debe estar entre 1 y 5. Has introducido " + stars + ".");
        }
        return null;
    }

    public void saveReview(Product product, Review review) {
        User currentUser = review.getUser();

        // This prevents null exception if the user doesn´t have a profile picture
        long imageId = -1L; // default value that indicates that the user doesnt have a profile picture
        if (currentUser.getImage() != null) {
            imageId = currentUser.getImage().getId();
        }
        review.setImageId(imageId);

        product.getReviews().add(review);
        productService.save(product);
    }

    public Review findReviewByIdAndCheckPermission(Long reviewId, String loggedInEmail, boolean isAdmin) {
        for (Product product : productService.findAll()) {
            for (Review review : product.getReviews()) {
                if (review.getId() == reviewId) {
                    if (review.getUser().getEmail().equals(loggedInEmail) || isAdmin) {
                        return review;
                    }
                    return null;
                }
            }
        }
        return null;
    }

    public Review findReviewById(long id) {
        Optional<Review> op = reviewRepository.findById(id);

        if (op.isPresent()) {
            return op.get();
        } else {
            throw new NoSuchElementException("Review " + id + " not found");
        }
    }

    // This method updates a review for the web service of the app
    // Due to previous design, this method has to return productID:
    public Long updateReview(Review updatedReview, String loggedInEmail) {

        for (Product product : productService.findAll()) {
            for (Review review : product.getReviews()) {
                // If we have found the review that we wanted to edit
                if (review.getId() == updatedReview.getId()) {

                    // if the user is an admin or the owner of the review
                    if (review.getUser().getEmail().equals(loggedInEmail)
                            || updatedReview.getUser().getRoles().indexOf("ADMIN") != -1) {
                        review.setTitle(updatedReview.getTitle());
                        review.setBody(updatedReview.getBody());
                        review.setStars(updatedReview.getStars());

                        LocalDateTime localDate = java.time.LocalDateTime.now();
                        DateTimeFormatter formatter = java.time.format.DateTimeFormatter
                                .ofPattern("dd/MM/yyyy HH:mm:ss");
                        review.setDate(localDate.format(formatter) + " (Editado)");

                        productService.save(product);
                        return product.getId();
                    } else {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                                "You can't update the review " + updatedReview.getId()
                                        + " because you are not the owner.");
                    }
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "There was an error server updating the review.");
    }

    public Review deleteReview(long reviewId, long productId, String loggedInEmail, boolean isAdmin) {

        Product product = productService.findById(productId);

        if (product != null) {
            // We search for the review we want to delete
            Review targetReview = null;
            for (Review review : product.getReviews()) {
                if (review.getId() == reviewId) {
                    targetReview = review;
                    break;
                }
            }

            if (targetReview == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review " + reviewId + " not found");
            }

            // we verify if we can delete it
            if (targetReview != null) {
                if (!targetReview.getUser().getEmail().equals(loggedInEmail) || isAdmin) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                            "You can't delete the review " + reviewId + " because you are not the owner.");
                }
            }

            // We remove the review from the list of reviews of the product and we delete it
            // from ddbb
            product.getReviews().remove(targetReview);
            productService.save(product);

            return targetReview;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review " + reviewId + " not found");
        }
    }

    public List<Review> findAllReviews() {
        return reviewRepository.findAll();
    }
}
