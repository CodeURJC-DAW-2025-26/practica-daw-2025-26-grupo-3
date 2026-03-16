package es.grupo3.practica25_26.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.model.Review;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.model.Error;
import es.grupo3.practica25_26.service.ErrorService;
import es.grupo3.practica25_26.service.ProductService;
import es.grupo3.practica25_26.service.ReviewService;
import es.grupo3.practica25_26.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private ErrorService errorService;

    @PostMapping("/add_review/{id}")
    public String addReview(Model model, @PathVariable long id, @RequestParam String title,
            @RequestParam String body,
            @RequestParam(required = false) Integer stars, HttpServletRequest request) {
        Product product = productService.findById(id);

        if (product == null) {
            return errorService.setErrorPageWithButton(model, null, "El producto no existe",
                    "Estás intentando publicar una reseña para un producto que no existe", "Volver al producto",
                    "/product_detail/" + id);
        }

        Error error = reviewService.reviewCheck(title, body, stars);
        if (error != null) {
            return errorService.setErrorPageWithButton(model, null, error.getTitle(), error.getMessage(), "Volver",
                    "/product_detail/" + id);
        }

        String email = request.getUserPrincipal().getName();
        User currentUser = userService.findUserByEmail(email);

        LocalDateTime localDate = java.time.LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        long userImageId = -1;
        if (currentUser.getImage() != null) {
            userImageId = currentUser.getImage().getId();
        }

        Review newReview = new Review(currentUser, title, body, localDate.format(formatter), stars,
                userImageId);
        reviewService.saveReview(product, newReview);

        return "redirect:/product_detail/" + id;
    }

    @GetMapping("/edit_review/{reviewId}")
    public String editReview(Model model, @PathVariable Long reviewId, HttpServletRequest request,
            HttpSession session) {

        if (request.getUserPrincipal() == null) {
            return "redirect:/login";
        }

        String loggedInEmail = request.getUserPrincipal().getName();
        boolean isAdmin = request.isUserInRole("ADMIN");

        // We asked the service to find the review for us.
        Review review = reviewService.findReviewByIdAndCheckPermission(reviewId, loggedInEmail, isAdmin);

        if (review != null) {
            model.addAttribute("review", review);
            return "edit_review";
        } else {
            return errorService.setErrorPageWithButton(model, session, "Error",
                    "La reseña no existe o no tienes permiso para editarla.",
                    "Volver al inicio", "/");
        }
    }

    @PostMapping("/edit_review/save/{reviewId}")
    public String saveEditedReview(Model model, @PathVariable Long reviewId,
            @RequestParam String title, @RequestParam String body,
            @RequestParam(required = false) Integer stars,
            HttpServletRequest request, HttpSession session) {

        if (request.getUserPrincipal() == null) {
            return "redirect:/login";
        }

        Error validationError = reviewService.reviewCheck(title, body, stars);

        // We check that there are no errors
        if (validationError != null) {
            return errorService.setErrorPageWithButton(model, session, validationError.getTitle(),
                    validationError.getMessage(), "Volver a intentar", "/edit_review/" + reviewId);
        }

        String loggedInEmail = request.getUserPrincipal().getName();
        User currentUser = userService.findUserByEmail(loggedInEmail);

        long userImageId = -1;
        if (currentUser.getImage() != null) {
            userImageId = currentUser.getImage().getId();
        }

        // We need to set the reviewId so the service can find the review to update
        Review reviewToUpdate = new Review(currentUser, title, body, null, stars, userImageId);
        reviewToUpdate.setId(reviewId);

        Long productId = reviewService.updateReview(reviewToUpdate, loggedInEmail);

        if (productId != null) {
            return "redirect:/product_detail/" + productId; // We return to product
        } else {
            return errorService.setErrorPageWithButton(model, session, "Error",
                    "No se pudo actualizar la reseña.", "Volver al inicio", "/");
        }
    }

    @PostMapping("/delete_review/{id}")
    public String deleteReview(Model model, @PathVariable long id, @RequestParam long productId,
            HttpServletRequest request, HttpSession session) {

        if (request.getUserPrincipal() == null) {
            return "redirect:/login";
        }

        String loggedInEmail = request.getUserPrincipal().getName();
        boolean isAdmin = request.isUserInRole("ADMIN");

        // ProductService deletes the product review
        Review deleted = reviewService.deleteReview(id, productId, loggedInEmail, isAdmin);

        // If the service couldn´t delete the review
        if (deleted == null) {
            return errorService.setErrorPageWithButton(model, session, "Error al borrar",
                    "No tienes permiso para borrar esta reseña o ya no existe.",
                    "Volver al producto", "/product_detail/" + productId);
        }

        return "redirect:/product_detail/" + productId;
    }

}
