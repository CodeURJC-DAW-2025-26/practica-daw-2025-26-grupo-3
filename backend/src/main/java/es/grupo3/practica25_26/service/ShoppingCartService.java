package es.grupo3.practica25_26.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.grupo3.practica25_26.model.CartItem;
import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.model.ShoppingCart;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.repository.CartItemRepository;
import es.grupo3.practica25_26.repository.ShoppingCartRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class ShoppingCartService {
    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

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

            List<CartItem> cartItems = cart.getCartItems();
            if (cartItems == null) {
                cartItems = new ArrayList<>();
            }

            cart.setCartItems(cartItems);
            CartItem newItem = new CartItem(product, 1, cartItems.size());
            cartItems.add(newItem);

            int productState = newItem.getProduct().getState();
            if (productState == 0) {
                newItem.setState("Nuevo");
            } else if (productState == 1) {
                newItem.setState("Reacondicionado");
            } else if (productState == 2) {
                newItem.setState("Segunda mano");
            }

            shoppingCartRepository.save(cart);
            userService.saveUser(user);
        }
    }

    public int getProductNumById(long id) {
        Optional<ShoppingCart> op = shoppingCartRepository.findById(id);

        if (!op.isPresent()) {
            return 0;
        } else {
            ShoppingCart cart = op.get();
            List<CartItem> items = cart.getCartItems();

            // We count how many products are in the cart, considering that we have to count
            // also products that will be purchased more than once.
            int item_counter = 0;
            for (CartItem i : items) {
                item_counter += i.getQuantity();
            }
            return item_counter;
        }
    }

    public double productPriceSum(long id) {
        Optional<ShoppingCart> op = shoppingCartRepository.findById(id);

        if (!op.isPresent()) {
            return 0;
        } else {
            ShoppingCart cart = op.get();
            List<CartItem> items = cart.getCartItems();
            double price_counter = 0;
            for (CartItem i : items) {
                price_counter += i.getProduct().getPrice() * i.getQuantity();
            }
            return price_counter;
        }
    }

    public void quantityUpdateByItemId(ShoppingCart cart, long itemId, int operation) throws Exception {
        Optional<CartItem> op = cartItemRepository.findById(itemId);

        if (!op.isPresent()) {
            throw new NullPointerException("No cart item found");
        } else {
            CartItem requierdItem = op.get();

            if (operation == 1) {
                requierdItem.setQuantity(requierdItem.getQuantity() + 1);
            } else if (operation == 0) {
                requierdItem.setQuantity(requierdItem.getQuantity() - 1);
            }

            cartItemRepository.save(requierdItem);
            shoppingCartRepository.save(cart);
        }
    }

    public void deleteCartItem(ShoppingCart cart, long itemId) throws Exception {
        Optional<CartItem> op = cartItemRepository.findById(itemId);

        if (!op.isPresent()) {
            throw new NullPointerException("No cart item found");
        } else {
            cartItemRepository.deleteById(itemId);
            shoppingCartRepository.save(cart);
        }
    }
}
