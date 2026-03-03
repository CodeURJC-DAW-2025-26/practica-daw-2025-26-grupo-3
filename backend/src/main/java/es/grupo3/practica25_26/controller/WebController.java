package es.grupo3.practica25_26.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.service.ErrorService;
import es.grupo3.practica25_26.service.ProductService;
import es.grupo3.practica25_26.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class WebController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private ErrorService errorService;

    // Loads the main landing page and populates it with all available products
    @GetMapping("/")
    public String index(Model model, HttpServletRequest request) {

        User currentUser = null;
        if (request.getUserPrincipal() != null) {
            String email = request.getUserPrincipal().getName();
            currentUser = userService.findUserByEmail(email);
        }

        if (currentUser != null && currentUser.getFavouriteState() != -1) {
            int userFavourite = currentUser.getFavouriteState();

            List<Product> favouriteProducts = productService.findTop8ByType(userFavourite);
            List<Product> allProducts = productService.findAll();

            allProducts.removeAll(favouriteProducts);

            List<Product> mergeList = new ArrayList<>();
            mergeList.addAll(favouriteProducts);
            mergeList.addAll(allProducts);

            int pageSize = 8;
            int totalElements = mergeList.size();

            int end = Math.min(pageSize, totalElements);

            List<Product> pageContent = mergeList.subList(0, end);

            Page<Product> featuredPage = new PageImpl<>(pageContent, PageRequest.of(0, pageSize), totalElements);

            model.addAttribute("products", featuredPage.getContent());
            model.addAttribute("isLast", featuredPage.isLast());

        } else {
            Page<Product> initialProducts = productService.getProductsPage(PageRequest.of(0, 8));

            model.addAttribute("products", initialProducts.getContent());
            model.addAttribute("isLast", initialProducts.isLast());
        }
        return "index";
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