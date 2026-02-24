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

        String email = request.getUserPrincipal().getName();
        User user = userService.findUserByEmail(email);
        model.addAttribute("address", user.getAddress());

        return "redirect:/shopping-cart";
    }

    @GetMapping("/shopping-cart")
    public String shoppingCart(Model model, HttpServletRequest request) {
        String email = request.getUserPrincipal().getName();
        User user = userService.findUserByEmail(email);
        ShoppingCart userCart = user.getShoppingCart();
        model.addAttribute("address", user.getAddress());

        if (userCart != null) {
            // We add the shopping cart entity attribute to template
            model.addAttribute("cart", userCart);

            // We add order sumary info (right part of screen)
            model.addAttribute("productNum", shoppingCartService.getProductNumById(userCart.getId()));
            model.addAttribute("total", shoppingCartService.productPriceSum(userCart.getId()));
        } else {
            model.addAttribute("productNum", 0);
            model.addAttribute("total", 0);
        }

        return "shopping-cart";
    }

    @PostMapping("/cart/add_unit/{operation}/{id}")
    public String addUnit(HttpServletRequest request, @PathVariable int operation,
            @PathVariable int id)
            throws Exception {
        String email = request.getUserPrincipal().getName();
        User user = userService.findUserByEmail(email);
        ShoppingCart userCart = user.getShoppingCart();

        shoppingCartService.quantityUpdateByItemId(userCart, id, operation);

        return "redirect:/shopping-cart";
    }

    @PostMapping("/cart/delete/{id}")
    public String deleteCartItem(HttpServletRequest request, @PathVariable long id) throws Exception {
        String email = request.getUserPrincipal().getName();
        User user = userService.findUserByEmail(email);
        ShoppingCart userCart = user.getShoppingCart();

        shoppingCartService.deleteCartItem(userCart, id);

        return "redirect:/shopping-cart";
    }
}