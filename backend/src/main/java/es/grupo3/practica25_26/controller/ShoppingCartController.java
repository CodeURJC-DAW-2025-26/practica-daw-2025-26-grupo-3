package es.grupo3.practica25_26.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.model.ShoppingCart;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.service.ErrorService;
import es.grupo3.practica25_26.service.OrderService;
import es.grupo3.practica25_26.service.ProductService;
import es.grupo3.practica25_26.service.ShoppingCartService;
import es.grupo3.practica25_26.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ErrorService errorService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    // Handles adding a product to the user's shopping cart
    @PostMapping("/cart/add/{id}")
    public String addProductToCart(Model model, @PathVariable("id") long productId, HttpServletRequest request) {
        Product product = productService.findById(productId);

        if (product != null) {
            shoppingCartService.saveProductToCart(productId, request);
        } else {
            return errorService.setErrorPageWithButton(model, null, "Producto no encontrado",
                    "El producto que intentas añadir no existe", "Volver al inicio", "/");
        }

        // Redirects to the cart view. The user and address will be loaded in the GET
        // mapping.
        return "redirect:/shopping-cart";
    }

    // Displays the user's shopping cart and its summary
    @GetMapping("/shopping-cart")
    public String shoppingCart(Model model, HttpServletRequest request) {
        String email = request.getUserPrincipal().getName();
        User user = userService.findUserByEmail(email);
        ShoppingCart userCart = user.getShoppingCart();

        model.addAttribute("address", user.getAddress());

        if (userCart != null) {
            // Add the shopping cart entity attribute to template
            model.addAttribute("cart", userCart);

            // Add order summary info (right part of screen)
            model.addAttribute("productNum", shoppingCartService.getProductNumById(userCart.getId()));
            model.addAttribute("total", shoppingCartService.productPriceSum(userCart.getId()));
        } else {
            model.addAttribute("productNum", 0);
            model.addAttribute("total", 0);
        }

        return "shopping-cart";
    }

    // Updates the quantity of a specific item in the shopping cart (add or
    // subtract)
    @PostMapping("/cart/modify_quantity/{operation}/{id}")
    public String modifyQuantity(HttpServletRequest request, @PathVariable int operation, @PathVariable int id)
            throws Exception {
        String email = request.getUserPrincipal().getName();
        User user = userService.findUserByEmail(email);
        ShoppingCart userCart = user.getShoppingCart();

        shoppingCartService.quantityUpdateByItemId(userCart, id, operation, user);

        return "redirect:/shopping-cart";
    }

    // Removes an item completely from the shopping cart
    @PostMapping("/cart/delete/{id}")
    public String deleteCartItem(HttpServletRequest request, @PathVariable long id) throws Exception {
        String email = request.getUserPrincipal().getName();
        User user = userService.findUserByEmail(email);
        ShoppingCart userCart = user.getShoppingCart();

        shoppingCartService.deleteCartItem(userCart, id);

        return "redirect:/shopping-cart";
    }

    // Converts the current shopping cart into a placed order
    @GetMapping("/cart/save_order")
    public String saveOrder(HttpServletRequest request) {
        String email = request.getUserPrincipal().getName();
        User user = userService.findUserByEmail(email);

        orderService.saveOrderByUser(user);
        userService.calculateFavouriteState(user);

        return "redirect:/profile";
    }
}