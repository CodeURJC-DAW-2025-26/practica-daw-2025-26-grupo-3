package es.grupo3.practica25_26.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        return "signup";
    }

    @GetMapping("/products-search-anonymous")
    public String productsSearchAnonymous(Model model) {
        return "products-search-anonymous";
    }

    @GetMapping("/index_registered")
    public String indexRegistered(Model model) {
        return "index_registered";
    }

    @GetMapping("/my_products")
    public String myProducts(Model model) {
        return "my_products";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        return "orders";
    }

    @GetMapping("/product-publish")
    public String productPublish(Model model) {
        return "product-publish";
    }

    @GetMapping("/products_detail_registered")
    public String productsDetailRegistered(Model model) {
        return "products_detail_registered";
    }

    @GetMapping("/products_detail_registered_new")
    public String productsDetailRegisteredNew(Model model) {
        return "products_detail_registered_new";
    }

    @GetMapping("/products-search-registered")
    public String productsSearchRegistered(Model model) {
        return "products-search-registered";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        return "profile";
    }

    @GetMapping("/shopping-cart")
    public String shoppingCart(Model model) {
        return "shopping-cart";
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

    @GetMapping("/products_published")
    public String productsPublished(Model model) {
        return "products_published";
    }

    @GetMapping("/user_registered_list")
    public String userRegisteredList(Model model) {
        return "user_registered_list";
    }
}
