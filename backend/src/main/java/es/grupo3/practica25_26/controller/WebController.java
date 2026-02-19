package es.grupo3.practica25_26.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.grupo3.practica25_26.model.Product;
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

    @GetMapping("/product_search")
    public String productSearch(Model model) {

        model.addAttribute("products", ProductService.findAll());
        return "product_search";
    }

    @GetMapping("/product_detail/{id}")
    public String productDetail(Model model,@PathVariable Long id) {

        Optional<Product> productOptional = ProductService.findById(id);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            model.addAttribute("product", product);
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

    @GetMapping("/shopping-cart")
    public String shoppingCart(Model model) {
        return "shopping-cart";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        return "orders";
    }

    @GetMapping("/my_products")
    public String productsPublished(Model model) {
        return "my_products";
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

    @GetMapping("/products-search-anonymous")
    public String productsSearchAnonymous(Model model) {
        return "products-search-anonymous";
    }

    @GetMapping("/admin_panel")
    public String adminPanel(Model model) {
        return "admin_panel";
    }

    @GetMapping("/admin_profile")
    public String adminProfile(Model model) {
        return "admin_profile";
    }

    @GetMapping("/edit_product_form_admin")
    public String editProductFormAdmin(Model model) {
        return "edit_product_form_admin";
    }

    @GetMapping("/new_product_form_admin")
    public String newProductFormAdmin(Model model) {
        return "new_product_form_admin";
    }

    @GetMapping("/orders_list")
    public String ordersList(Model model) {
        return "orders_list";
    }

    @GetMapping("/product_detail_admin")
    public String productDetailAdmin(Model model) {
        return "product_detail_admin";
    }

    @GetMapping("/products_pending_list")
    public String productsPendingList(Model model) {
        return "products_pending_list";
    }

    @GetMapping("/user_registered_list")
    public String userRegisteredList(Model model) {
        return "user_registered_list";
    }
}
