package es.grupo3.practica25_26.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.model.Review;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.service.ErrorService;
import es.grupo3.practica25_26.service.ProductService;
import es.grupo3.practica25_26.service.UserService;
import es.grupo3.practica25_26.model.Error;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    ErrorService errorService;

    @GetMapping("/product_search")
    public String productSearch(Model model) {

        model.addAttribute("products", productService.findAll());
        return "product_search";
    }

    @GetMapping("/product_detail/{id}")
    public String productDetail(Model model, @PathVariable Long id, HttpServletRequest request) {

        Optional<Product> productOptional = productService.findById(id);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            model.addAttribute("product", product);

            boolean canModifyProduct = false;
            boolean isAdmin = false;
            String loggedInEmail = null;
            
            if (request.getUserPrincipal() != null) {

                // Get the email of the logged-in user and the seller's email to compare them
                loggedInEmail = request.getUserPrincipal().getName();
                String sellerEmail = product.getSeller().getEmail();
                isAdmin = request.isUserInRole("ADMIN");

                // If the user is an admin, or a owner of the product, he can edit or remove it
                if (loggedInEmail.equals(sellerEmail) || isAdmin) {
                    canModifyProduct = true;
                }
            }  
            //loop that shows or not the edit and delete button depending on the user
            for (Review review : product.getReviews()) {
                //if the user is an admin or the owner of the review then he can edit or delete it
                if (isAdmin || (loggedInEmail != null && review.getUser().getEmail().equals(loggedInEmail))) {
                    review.setCanModifyReview(true); 
                } else {
                    review.setCanModifyReview(false); 
                }
            }

            model.addAttribute("canModifyProduct", canModifyProduct);
            model.addAttribute("id", id);
            model.addAttribute("reviews", product.getReviews());
            model.addAttribute("images", false);

            return "product_detail";
        } else {
            // If the product does not exist, we redirect to the homepage or an error page.
            return "redirect:/";
        }
    }

    @GetMapping("/product-publish")
    public String productPublish(Model model) {
        return "product-publish";
    }

    @GetMapping("/products_published")
    public String productsPublished(Model model) {
        model.addAttribute("products", productService.findAll());
        return "products_published";
    }

    @GetMapping("/product_detail_admin")
    public String productDetailAdmin(Model model) {
        return "product_detail_admin";
    }

    @GetMapping("/products_pending_list")
    public String productsPendingList(Model model) {
        return "products_pending_list";
    }

    @GetMapping("/edit_product/{id}")
    public String editProduct(Model model, @PathVariable Long id, HttpServletRequest request, HttpSession session) {

        Optional<Product> productOpt = productService.findById(id);

        if (productOpt.isPresent() && request.getUserPrincipal() != null) {

            Product product = productOpt.get();
            String loggedInEmail = request.getUserPrincipal().getName();

            // Comprobation if the user is an admin
            boolean isAdmin = request.isUserInRole("ADMIN");

            // If the user is an admin or the owner of the product
            if (product.getSeller().getEmail().equals(loggedInEmail) || isAdmin) {

                model.addAttribute("product", product);
                return "edit_product";

            } else {
                return errorService.setErrorPageWithButton(model, session, "No autorizado",
                        "No tienes permiso para editar este producto.",
                        "Volver a mis productos",
                        "/my_products");
            }
        }
        return errorService.setErrorPageWithButton(model, session, "Producto no encontrado",
                "El producto que intentas editar no existe o no has iniciado sesión.",
                "Volver al inicio",
                "/");
    }

    @PostMapping("/edit_product/save/{id}")
    public String saveEditedProduct(Model model, @PathVariable Long id, @ModelAttribute("product") Product productForm,
            @RequestParam(value = "removeImages", required = false) List<Long> removeImages,
            @RequestParam(value = "productimages", required = false) List<MultipartFile> newImages,
            HttpServletRequest request, HttpSession session) {

        Optional<Product> actualProductOpt = productService.findById(id);

        if (actualProductOpt.isPresent() && request.getUserPrincipal() != null) {

            Product existingProduct = actualProductOpt.get();

            // We validate all the attributes of the edited product
            Error validationError = productService.productUpdateCheck(productForm, existingProduct, removeImages,
                    newImages);
            // if there is any error, we show it in a error page
            if (validationError != null) {
                return errorService.setErrorPageWithButton(
                        model, session, validationError.getTitle(), validationError.getMessage(),
                        "Volver a la edición", "/edit_product/" + id);
            }
            // If there is no error, we try to update the product
            String loggedInEmail = request.getUserPrincipal().getName();
            boolean isAdmin = request.isUserInRole("ADMIN");

            if (!existingProduct.getSeller().getEmail().equals(loggedInEmail) && !isAdmin) {
                return errorService.setErrorPageWithButton(
                        model,
                        session,
                        "No autorizado",
                        "No tienes permiso para editar este producto.",
                        "Volver a mis productos",
                        "/my_products");
            }

            try {
                productService.updateProduct(id, productForm, removeImages, newImages, loggedInEmail, isAdmin);
                return "redirect:/product_detail/" + id;
            } catch (Exception e) {
                return errorService.setErrorPageWithButton(
                        model, session, "Error de base de datos",
                        "La base de datos no acepta estos datos: " + e.getMessage(),
                        "Volver a intentar", "/edit_product/" + id);
            }
        }
        return errorService.setErrorPageWithButton(
                model, session, "Error",
                "El producto no existe o no has iniciado sesión.",
                "Volver al inicio",
                "/");
    }

    @GetMapping("/new_product_form_admin")
    public String newProductFormAdmin(Model model) {
        return "new_product_form_admin";
    }

    @PostMapping("/publish_new_product")
    public String publishNewProduct(Model model, Product product,
            @RequestParam("productimages") List<MultipartFile> productImages, HttpSession session,
            HttpServletRequest request) {

        if (request.getUserPrincipal() != null) {
            Error validationError = productService.productCreateCheck(product, productImages);

            // If there is any error (empty name, negative price ...)
            if (validationError != null) {
                return errorService.setErrorPageWithButton(
                        model, session, validationError.getTitle(), validationError.getMessage(),
                        "Volver al formulario", "/product-publish");
            }

            // We try to save the product
            try {
                String email = request.getUserPrincipal().getName();

                // ProductService creates and saves the new product
                productService.createNewProduct(product, productImages, email);
            } catch (IOException e) {

                return errorService.setErrorPageWithButton(
                        model,
                        session,
                        "Error al publicar el producto",
                        "Hubo un problema al procesar o guardar las imágenes de tu producto. Por favor, revisa que el archivo sea válido e inténtalo de nuevo.",
                        "Volver a intentar",
                        "/product-publish");
            }
        }
        return "redirect:/product_search";
    }

    @PostMapping("/delete_product/{id}")
    public String deleteProduct(Model model, HttpSession session, @PathVariable Long id, HttpServletRequest request) {

        if (request.getUserPrincipal() != null) {
            String loggedInEmail = request.getUserPrincipal().getName();
            boolean isAdmin = request.isUserInRole("ADMIN");

            boolean deleted = productService.deleteProduct(id, loggedInEmail, isAdmin);

            if (!deleted) {
                return errorService.setErrorPageWithButton(
                        model,
                        session,
                        "No autorizado",
                        "No tienes permiso para eliminar este producto o el producto no existe.",
                        "Volver a mis productos",
                        "/my_products");
            }
        }
        return "redirect:/";
    }
    
    @GetMapping("/my_products")
    public String myProducts(Model model, HttpServletRequest request) {
        String email = request.getUserPrincipal().getName();
        User user = userService.findUserByEmail(email);
        model.addAttribute("products", user.getProducts());
        return "my_products";
    }

    /**REVIEWS **/

    @PostMapping("/add_review/{id}")
    public String addReview(Model model, @PathVariable long id, @RequestParam String title,
            @RequestParam String body,
            @RequestParam(required = false) Integer stars, HttpServletRequest request) {
        Optional<Product> op = productService.findById(id);

        if (!op.isPresent()) {
            return errorService.setErrorPageWithButton(model, null, "El producto no existe",
                    "Estás intentando publicar una reseña para un producto que no existe", "Volver al producto",
                    "/product_detail/" + id);
        }



        Error error = productService.reviewCreateCheck(title, body, stars);
        if (error != null) {
            return errorService.setErrorPageWithButton(model, null, error.getTitle(), error.getMessage(), "Volver",
                    "/product_detail/" + id);
        }

        Product product = op.get();
        productService.saveReview(product, title, body, stars, request);
        return "redirect:/product_detail/" + id;
    }

    @GetMapping("/edit_review/{reviewId}")
    public String editReview(Model model, @PathVariable Long reviewId, HttpServletRequest request, HttpSession session) {
        
        if (request.getUserPrincipal() == null) {
            return "redirect:/login"; 
        }

        String loggedInEmail = request.getUserPrincipal().getName();
        boolean isAdmin = request.isUserInRole("ADMIN");

        // We asked the service to find the review for us. 
        Review review = productService.findReviewByIdAndCheckPermission(reviewId, loggedInEmail, isAdmin);

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

       
        Error validationError = productService.reviewCreateCheck(title, body, stars);
        
        //We check that there are no errors
        if (validationError != null) {
            return errorService.setErrorPageWithButton(model, session, validationError.getTitle(), 
                    validationError.getMessage(), "Volver a intentar", "/edit_review/" + reviewId);
        }

        
        String loggedInEmail = request.getUserPrincipal().getName();
        boolean isAdmin = request.isUserInRole("ADMIN");
        // We save the changes
        Long productId = productService.updateReview(reviewId, title, body, stars, loggedInEmail, isAdmin);

        if (productId != null) {
            return "redirect:/product_detail/" + productId; // Volvemos al producto
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
        boolean deleted = productService.deleteReview(id, productId, loggedInEmail, isAdmin);

        // If the service couldn´t delete the review
        if (!deleted) {
            return errorService.setErrorPageWithButton(model, session, "Error al borrar", 
                    "No tienes permiso para borrar esta reseña o ya no existe.", 
                    "Volver al producto", "/product_detail/" + productId);
        }
        
        
        return "redirect:/product_detail/" + productId;
    }

}
