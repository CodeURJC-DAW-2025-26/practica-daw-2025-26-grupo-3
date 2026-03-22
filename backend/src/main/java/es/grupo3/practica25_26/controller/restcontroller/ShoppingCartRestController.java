package es.grupo3.practica25_26.controller.restcontroller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import es.grupo3.practica25_26.dto.CartItemDTO;
import es.grupo3.practica25_26.dto.CartItemRequestDTO;
import es.grupo3.practica25_26.dto.ShoppingCartDTO;
import es.grupo3.practica25_26.mapper.CartItemMapper;
import es.grupo3.practica25_26.mapper.ShoppingCartMapper;
import es.grupo3.practica25_26.model.ShoppingCart;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.service.ShoppingCartService;
import es.grupo3.practica25_26.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/carts")
public class ShoppingCartRestController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private CartItemMapper cartItemMapper;

    @Operation(summary = "Get my shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shopping cart retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Shopping cart not found")
    })
    @GetMapping("/me")
    public ResponseEntity<ShoppingCartDTO> getMyCart(HttpServletRequest request) {

        // Obtain the user that is logged in
        String email = request.getUserPrincipal().getName();

        // Find the user by email and if it doesn't exist, throw an exception
        User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new NullPointerException("User not found");
        }

        // If the user doesn't have a shopping cart, return a 404 not found response
        if (user.getShoppingCart() == null || user.getShoppingCart().getCartItems().isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // If the user has a cart, we use the shoppingCartMapper to convert the cart to
        // a DTO
        ShoppingCartDTO cartDTO = shoppingCartMapper.toDTO(user.getShoppingCart());

        // Return a 200 OK response with the cart DTO
        return ResponseEntity.ok(cartDTO);
    }

    @Operation(summary = "Add product to cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product added to cart"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PostMapping("/me/items")
    public ResponseEntity<ShoppingCartDTO> addProductToCart(@RequestBody CartItemRequestDTO productRequest,
            HttpServletRequest request) {
        // we call the service method to save the product to the cart
        shoppingCartService.saveProductToCart(productRequest.productId(), request);

        // return the updated cart
        String email = request.getUserPrincipal().getName();
        User user = userService.findUserByEmail(email);
        return ResponseEntity.ok(shoppingCartMapper.toDTO(user.getShoppingCart()));
    }

    @Operation(summary = "Remove product from cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product removed from cart"),
            @ApiResponse(responseCode = "404", description = "Cart or item not found")
    })
    @DeleteMapping("/me/items/{itemId}")
    public ResponseEntity<ShoppingCartDTO> removeProductFromCart(@PathVariable long itemId, HttpServletRequest request)
            throws Exception {
        // Obtain the user that is logged in
        String email = request.getUserPrincipal().getName();

        // Find the user by email and if it doesn't exist, throw an exception
        User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new NullPointerException("User not found");
        }

        // If the user doesn't have a shopping cart, return a 404 not found response
        if (user.getShoppingCart() == null) {
            return ResponseEntity.notFound().build();
        }

        // we call the service method to remove the product from the cart
        shoppingCartService.deleteCartItem(user.getShoppingCart(), itemId);

        // return the updated cart
        return ResponseEntity.ok(shoppingCartMapper.toDTO(user.getShoppingCart()));
    }

    @Operation(summary = "Modify item quantity in cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantity modified successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid operation or item not in cart"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    @PutMapping("/me/items/{cartItemId}")
    public ResponseEntity<CartItemDTO> modifyQuantity(HttpServletRequest request, @PathVariable long cartItemId,
            @RequestBody Map<String, Integer> body) throws Exception {
        Integer operation = body.get("operation");
        if (operation == null || operation < 0 || operation > 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must send a JSON with the field 'operations'. Available operations are:  0: Substract | 1: Add");
        }

        String loggedEmail = request.getUserPrincipal().getName();
        User loggedUser = userService.findUserByEmail(loggedEmail);
        ShoppingCart userCart = loggedUser.getShoppingCart();

        return ResponseEntity.ok(
                cartItemMapper.toDTO(
                        shoppingCartService.quantityUpdateByItemId(userCart, cartItemId, operation, loggedUser)));
    }
}