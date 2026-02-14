package es.grupo3.practica25_26.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.service.ProductService;
import es.grupo3.practica25_26.service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class WebController {

    @Autowired
    ProductService ProductService;

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        userService.getUserNavInfo(model, session);
        model.addAttribute("products", ProductService.findAll());
        return "index";
    }

    @GetMapping("/product_search")
    public String productSearch(Model model, HttpSession session) {
        userService.getUserNavInfo(model, session);
        model.addAttribute("products", ProductService.findAll());
        return "product_search";
    }

    @GetMapping("/product_detail")
    public String productDetail(Model model, HttpSession session) {
        userService.getUserNavInfo(model, session);
        return "product_detail";
    }

    @GetMapping("/product-publish")
    public String productPublish(Model model, HttpSession session) {
        userService.getUserNavInfo(model, session);
        return "product-publish";
    }

    @GetMapping("/shopping-cart")
    public String shoppingCart(Model model, HttpSession session) {
        userService.getUserNavInfo(model, session);
        return "shopping-cart";
    }

    @GetMapping("/orders")
    public String orders(Model model, HttpSession session) {
        userService.getUserNavInfo(model, session);
        return "orders";
    }

    @GetMapping("/my_products")
    public String productsPublished(Model model, HttpSession session) {
        userService.getUserNavInfo(model, session);
        return "my_products";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/signup")
    public String signup(Model model, HttpSession session) {
        if (session.getAttribute("user_failed_register") != null) {
            model.addAttribute("newUser", session.getAttribute("user_failed_register"));
            session.removeAttribute("user_failed_register");
        }
        return "signup";
    }

    @GetMapping("/products-search-anonymous")
    public String productsSearchAnonymous(Model model) {
        return "products-search-anonymous";
    }

    @GetMapping("/index_registered")
    public String indexRegistered(Model model) {
        model.addAttribute("products", ProductService.findAll());
        return "index_registered";
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
