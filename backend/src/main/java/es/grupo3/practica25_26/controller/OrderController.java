package es.grupo3.practica25_26.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping; // Added import for PostMapping
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletRequest;
import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.model.ShoppingCart;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.service.ErrorService;
import es.grupo3.practica25_26.service.ProductService;
import es.grupo3.practica25_26.service.ShoppingCartService;
import es.grupo3.practica25_26.service.UserService;

@Controller
public class OrderController {

    @Autowired
    ProductService productService;

    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    UserService userService;

    @Autowired
    ErrorService errorService;

    @PostMapping("/cart/add/{id}") // Changed to POST to match form method
    public String addProductToCart(Model model, @PathVariable("id") long productId, HttpServletRequest request) {

        Optional<Product> op = productService.findById(productId);
        if (op.isPresent()) {
            shoppingCartService.saveProductToCart(productId, request);
        } else {
            return errorService.setErrorPageWithButton(model, null, "Producto no encontrado",
                    "El producto que intentas añadir no existe", "Volver al inicio", "/");
        }

        return "shopping-cart";
    }

    @GetMapping("/shopping-cart")
    public String shoppingCart(Model model, HttpServletRequest request) {
        String email = request.getUserPrincipal().getName();
        User user = userService.findUserByEmail(email);

        ShoppingCart userCart = user.getShoppingCart();
        model.addAttribute("cart", userCart);

        return "shopping-cart";
    }
}
