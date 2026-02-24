package es.grupo3.practica25_26.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.grupo3.practica25_26.model.Image;
import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.service.ProductService;
import es.grupo3.practica25_26.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ProductController {

    @Autowired
    ProductService ProductService;

    @Autowired
    UserService userService;

    @GetMapping("/product_search")
    public String productSearch(Model model) {

        model.addAttribute("products", ProductService.findAll());
        return "product_search";
    }

    @GetMapping("/product_detail/{id}")
    public String productDetail(Model model, @PathVariable Long id, HttpServletRequest request) {

        Optional<Product> productOptional = ProductService.findById(id);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            model.addAttribute("product", product);

            boolean isOwner = false;
            if (request.getUserPrincipal() != null) {

                // Get the email of the logged-in user and the seller's email to compare them
                String loggedInEmail = request.getUserPrincipal().getName();
                String sellerEmail = product.getSeller().getEmail();

                if (loggedInEmail.equals(sellerEmail)) {
                    isOwner = true;
                }
            }
            // Le pasamos la variable a Mustache
            model.addAttribute("isOwner", isOwner);

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
    public String publishNewProduct(Model model, Product product,
            @RequestParam("productimages") List<MultipartFile> Productimages, HttpServletRequest request)
            throws IOException {

        String email = request.getUserPrincipal().getName();

        User seller = userService.findUserByEmail(email);

        product.setSeller(seller);

        List<Image> productImages = new ArrayList<>();

        for (MultipartFile file : Productimages) {

            if (!file.isEmpty()) {

                Image image = new Image();
                try {
                    image.setImageFile(new SerialBlob(file.getBytes()));
                } catch (Exception e) {
                    throw new IOException("Failed to create image", e);
                }

                productImages.add(image);

            }

        }

        // assign the images to the product
        product.setImages(productImages);

        ProductService.save(product);

        return "redirect:/product_search";
    }

    @PostMapping("/delete_product/{id}")
    public String deleteProduct(@PathVariable Long id, HttpServletRequest request) {

        Optional<Product> productOpt = ProductService.findById(id);

        if (productOpt.isPresent() && request.getUserPrincipal() != null) {

            Product product = productOpt.get();
            String loggedInEmail = request.getUserPrincipal().getName();
            User seller = product.getSeller();
            String sellerEmail = seller.getEmail();

            // unlink the relationship between user and his product before deleting it
            if (seller.getProducts() != null) {
                seller.getProducts().remove(product);
            }

            System.out.println("Usuario logueado: " + loggedInEmail);
            System.out.println("Dueño del producto: " + sellerEmail);

            // prove that seller email and logged email are equal
            if (sellerEmail.equals(loggedInEmail)) {
                ProductService.deleteById(id);
            }
        }

        return "redirect:/product_search";

    }

}
