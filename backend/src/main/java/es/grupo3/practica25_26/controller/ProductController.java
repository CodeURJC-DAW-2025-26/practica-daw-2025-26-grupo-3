package es.grupo3.practica25_26.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.service.ProductService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {

    @Autowired
    ProductService ProductService;

    @GetMapping("/product_search")
    public String productSearch(Model model) {

        model.addAttribute("products", ProductService.findAll());
        return "product_search";
    }

    @GetMapping("/product_detail/{id}")
    public String productDetail(Model model, @PathVariable Long id) {

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

    @GetMapping("/my_products")
    public String myPublishedProducts(Model model) {
        return "my_products";
    }

    @GetMapping("/products_published")
    public String productsPublished(Model model) {
        model.addAttribute("products", ProductService.findAll());
        return "products_published";
    }

    @GetMapping("/products-search-anonymous")
    public String productsSearchAnonymous(Model model) {
        return "products-search-anonymous";
    }

    @GetMapping("/product_detail_admin")
    public String productDetailAdmin(Model model) {
        return "product_detail_admin";
    }

    @GetMapping("/products_pending_list")
    public String productsPendingList(Model model) {
        return "products_pending_list";
    }

    @GetMapping("/edit_product_form_admin")
    public String editProductFormAdmin(Model model) {
        return "edit_product_form_admin";
    }

    @GetMapping("/new_product_form_admin")
    public String newProductFormAdmin(Model model) {
        return "new_product_form_admin";
    }

    @PostMapping("/publish_new_product")
    public String publishNewProduct(Model model, @RequestParam Product product) {

        ProductService.save(product);

        return "redirect:/products_published";
    }

}
