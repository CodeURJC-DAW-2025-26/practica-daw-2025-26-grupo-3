package es.grupo3.practica25_26.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.service.ErrorService;
import es.grupo3.practica25_26.service.ProductService;
import es.grupo3.practica25_26.service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class WebController {

    @Autowired
    ProductService ProductService;

    @Autowired
    UserService userService;

    @Autowired
    ErrorService errorService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("products", ProductService.findAll());
        return "index";
    }

    @GetMapping("/shopping-cart")
    public String shoppingCart(Model model) {
        return "shopping-cart";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        return "orders";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("error", false);
        return "login";
    }

    @GetMapping("/loginerror")
    public String loginError(Model model, HttpSession session) {
        return errorService.setErrorPageWithButton(model, session, "Error de inicio de sesión",
                "Usuario o contraseña incorrectos", "Volver a intentar", "/login");
    }

    @GetMapping("/signup")
    public String signup(Model model, HttpSession session) {
        User failedUser = (User) session.getAttribute("user_failed_register");
        if (failedUser != null) {
            model.addAttribute("newUser", failedUser);
            session.removeAttribute("user_failed_register");
        }
        return "signup";
    }

    @GetMapping("/admin_panel")
    public String adminPanel(Model model) {
        return "admin_panel";
    }

    @GetMapping("/admin_profile")
    public String adminProfile(Model model) {
        return "admin_profile";
    }

    @GetMapping("/orders_list")
    public String ordersList(Model model) {
        return "orders_list";
    }

    // Obtain only the users, not the admins
    /*
     * @GetMapping("/user_registered_list")
     * public String userRegisteredList(Model model) {
     * model.addAttribute("users", userService.getUsersWithoutAdmins());
     * return "user_registered_list";
     * }
     */

}
