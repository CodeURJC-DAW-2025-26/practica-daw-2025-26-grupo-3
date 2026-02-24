package es.grupo3.practica25_26.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.model.ShoppingCart;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.repository.ShoppingCartRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class ShoppingCartService {
    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    public void saveProductToCart(long id, HttpServletRequest request) throws NullPointerException {
        Optional<Product> opProduct = productService.findById(id);
        String email = request.getUserPrincipal().getName();
        User user;

        try {
            user = userService.findUserByEmail(email);
        } catch (NullPointerException e) {
            throw new NullPointerException("User not found");
        }

        if (!opProduct.isPresent()) {
            throw new NullPointerException("Product not found");
        } else {
            Product product = opProduct.get();
            ShoppingCart cart;

            if (user.getShoppingCart() != null) {
                cart = user.getShoppingCart();
            } else {
                cart = new ShoppingCart();
                user.setShoppingCart(cart);
            }

            Map<Product, Long> cartMap = cart.getProducts(); // Get existing products
            if (cartMap == null) {
                cartMap = new HashMap<>();
            }

            cartMap.put(product, (long) 1);
            cart.setProducts(cartMap);
            shoppingCartRepository.save(cart);
            userService.saveUser(user);
        }
    }
}
