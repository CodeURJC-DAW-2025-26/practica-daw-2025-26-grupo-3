package es.grupo3.practica25_26.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.grupo3.practica25_26.model.User;
import jakarta.servlet.http.HttpSession;

@Controller
public class WebController {

    private void getUserNavInfo(Model model, HttpSession sesion) {
        User currentUser;
        boolean user_logged = true;
        try {
            user_logged = (boolean) sesion.getAttribute("user_logged");
            currentUser = (User) sesion.getAttribute("currentUser");
            model.addAttribute("userName", currentUser.getUserName());
        } catch (Exception e) { // If we get no data from sesion, then we asume that there is no logged user.
            user_logged = false;
        }
        model.addAttribute("user_logged", user_logged);
    }

    @GetMapping("/")
    public String index(Model model, HttpSession sesion) {
        getUserNavInfo(model, sesion);
        return "index";
    }

    @GetMapping("/product_search")
    public String productSearch(Model model, HttpSession sesion) {
        getUserNavInfo(model, sesion);
        return "product_search";
    }

    @GetMapping("/product_detail")
    public String productDetail(Model model, HttpSession sesion) {
        getUserNavInfo(model, sesion);
        return "product_detail";
    }

    @GetMapping("/product-publish")
    public String productPublish(Model model, HttpSession sesion) {
        getUserNavInfo(model, sesion);
        return "product-publish";
    }

    @GetMapping("/shopping-cart")
    public String shoppingCart(Model model, HttpSession sesion) {
        getUserNavInfo(model, sesion);
        return "shopping-cart";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        return "signup";
    }

    @GetMapping("/my_products")
    public String myProducts(Model model) {
        return "my_products";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        return "orders";
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
