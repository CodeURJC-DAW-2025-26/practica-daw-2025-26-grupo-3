package es.grupo3.practica25_26.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletRequest;

// CRITICAL FIX: Changed the import from jakarta.persistence.criteria.Order to your custom model
import es.grupo3.practica25_26.model.Order;
import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.model.ShoppingCart;
import es.grupo3.practica25_26.model.User;

import es.grupo3.practica25_26.service.ErrorService;
import es.grupo3.practica25_26.service.OrderService;
import es.grupo3.practica25_26.service.ProductService;
import es.grupo3.practica25_26.service.ShoppingCartService;
import es.grupo3.practica25_26.service.UserService;

@Controller
public class OrderController {

    private final ProductService productService;
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;
    private final OrderService orderService;
    private final ErrorService errorService;

    // Dependency injection via constructor
    public OrderController(ProductService productService, ShoppingCartService shoppingCartService,
            UserService userService, OrderService orderService, ErrorService errorService) {
        this.productService = productService;
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
        this.orderService = orderService;
        this.errorService = errorService;
    }

    // Handles adding a product to the user's shopping cart
    @PostMapping("/cart/add/{id}")
    public String addProductToCart(Model model, @PathVariable("id") long productId, HttpServletRequest request) {
        Optional<Product> op = productService.findById(productId);

        if (op.isPresent()) {
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
    @PostMapping("/cart/add_unit/{operation}/{id}")
    public String addUnit(HttpServletRequest request, @PathVariable int operation, @PathVariable int id)
            throws Exception {
        String email = request.getUserPrincipal().getName();
        User user = userService.findUserByEmail(email);
        ShoppingCart userCart = user.getShoppingCart();

        shoppingCartService.quantityUpdateByItemId(userCart, id, operation);

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

        return "redirect:/profile";
    }

    // Handles the admin action to accept an order, marking it as Delivered (0)
    @PostMapping("/orders/{id}/accept")
    public String acceptOrder(Model model, @PathVariable long id) {
        Optional<Order> op = orderService.findById(id);

        if (op.isPresent()) {
            Order order = op.get();
            order.setState(0); // Update state to Delivered
            order.setStateText("Entregado"); // Corrected text based on state 0
            orderService.save(order); // Changed from saveOrder to sSave to match OrderService methods

            return "redirect:/orders_list"; // Changed to orders_list where this button is located
        } else {
            return errorService.setErrorPageWithButton(model, null, "Pedido no encontrado",
                    "El pedido que intentas aceptar no existe", "Volver a pedidos", "/orders_list");
        }
    }
}