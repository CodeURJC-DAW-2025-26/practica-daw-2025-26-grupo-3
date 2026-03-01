package es.grupo3.practica25_26.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.service.ErrorService;
import es.grupo3.practica25_26.service.OrderService;
import es.grupo3.practica25_26.service.ProductService;
import es.grupo3.practica25_26.service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class WebController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private ErrorService errorService;

    @Autowired
    private OrderService orderService;

    // Loads the main landing page and populates it with all available products
    @GetMapping("/")
    public String index(Model model) {
        // we obtain the first 8 products
        Page<Product> initialProducts = productService.getProductsPage(PageRequest.of(0, 8));

        model.addAttribute("products", initialProducts.getContent());
        model.addAttribute("isLast", initialProducts.isLast());
        return "index";
    }

    @GetMapping("/products/more")
    public String loadMoreProducts(@RequestParam int page, Model model) {
        // We request the next page, size 4
        Page<Product> productPage = productService.getProductsPage(PageRequest.of(page, 4));

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("isLast", productPage.isLast());

        // returns a html fragment with the new loaded products
        return "product";
    }

    // Displays the login form, initializing the error flag to false by default
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("error", false);
        return "login";
    }

    // Handles login failures by displaying a standardized error page via
    // ErrorService
    @GetMapping("/loginerror")
    public String loginError(Model model, HttpSession session) {
        return errorService.setErrorPageWithButton(model, session, "Error de inicio de sesión",
                "Usuario o contraseña incorrectos", "Volver a intentar", "/login");
    }

    // Loads the signup form, restoring previously entered data if a registration
    // attempt failed
    @GetMapping("/signup")
    public String signup(Model model, HttpSession session) {
        User failedUser = (User) session.getAttribute("user_failed_register");
        if (failedUser != null) {
            model.addAttribute("newUser", failedUser);
            session.removeAttribute("user_failed_register");
        }
        return "signup";
    }

    // Renders the main dashboard panel for administrators
    @GetMapping("/admin_panel")
    public String adminPanel() {
        return "admin_panel";
    }

    // Displays the profile settings page for administrators
    @GetMapping("/admin_profile")
    public String adminProfile() {
        return "admin_profile";
    }

    // Fetches and displays a list of orders that are either pending or revised for
    // admin review
    @GetMapping("/orders_list")
    public String ordersList(Model model) {
        model.addAttribute("orders", orderService.findPendingAndRevisedOrdersWithUser());
        return "orders_list";
    }

    // Retrieves and displays a list of all standard registered users, excluding
    // administrators
    @GetMapping("/user_registered_list")
    public String userRegisteredList(Model model) {
        model.addAttribute("users", userService.getUsersWithoutAdmins());
        return "user_registered_list";
    }

    // Displays an access denied error page specifically for users who have been
    // blocked
    @GetMapping("/blocked_user_error")
    public String usuarioBloqueado(Model model, HttpSession session) {
        return errorService.setErrorPageWithButton(
                model,
                session,
                "¡Acceso Denegado!",
                "Tu cuenta ha sido bloqueada por un administrador. Si crees que se trata de un error, contacta con soporte.",
                "Volver al inicio",
                "/");
    }

    @GetMapping("/access_denied")
    public String accessDenied(Model model, HttpSession session) {
        return errorService.setErrorPageWithButton(
                model,
                session,
                "Acceso Denegado",
                "No tienes permisos de administrador para acceder a esta página.",
                "Volver al inicio",
                "/");
    }
}