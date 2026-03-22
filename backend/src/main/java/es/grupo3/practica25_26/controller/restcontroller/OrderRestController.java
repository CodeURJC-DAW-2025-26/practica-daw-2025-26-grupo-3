package es.grupo3.practica25_26.controller.restcontroller;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.grupo3.practica25_26.dto.OrderDTO;
import es.grupo3.practica25_26.mapper.OrderMapper;
import es.grupo3.practica25_26.model.Order;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.service.BillService;
import es.grupo3.practica25_26.service.OrderService;
import es.grupo3.practica25_26.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderRestController {

    @Autowired
    private BillService billService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @Operation(summary = "Get PDF for a specific order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden (Not user's order)")
    })
    @GetMapping("/{orderId}/bill")
    public ResponseEntity<byte[]> getOrderPdf(@PathVariable long orderId, HttpServletRequest request)
            throws IOException {
        String email = request.getUserPrincipal().getName();
        User user = userService.findUserByEmail(email);

        return billService.orderPdfConfig(orderId, user);
    }

    @Operation(summary = "Get PDF for all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF of all orders retrieved successfully")
    })
    @GetMapping("/all/bill")
    public ResponseEntity<byte[]> getAllOrderPdf(HttpServletRequest request) throws IOException {
        String currentEmail = request.getUserPrincipal().getName();
        User currentUser = userService.findUserByEmail(currentEmail);
        return billService.allOrdersPdfConfig(currentUser);
    }

    @Operation(summary = "Get all orders (Admin)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All orders retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden (Admin only)")
    })
    @GetMapping
    public Collection<OrderDTO> getAllOrders(HttpServletRequest request) {
        if (!request.isUserInRole("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Only administators can list all orders.");
        }

        return orderMapper.toDTOs(orderService.getAllOrders());
    }

    @Operation(summary = "Get current user orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User orders retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden (User not found)")
    })
    @GetMapping("/me")
    public Collection<OrderDTO> getAllUserOrders(HttpServletRequest request) {
        String loggedEmail = request.getUserPrincipal().getName();
        User loggedUser = userService.findUserByEmail(loggedEmail);

        if (loggedUser == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "User not found.");
        }

        return orderMapper.toDTOs(loggedUser.getOrders());
    }

    @Operation(summary = "Create order from shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized (User not logged in)"),
            @ApiResponse(responseCode = "404", description = "Cart is empty or not found")
    })
    @PostMapping
    public ResponseEntity<OrderDTO> shoppingCartToOrder(HttpServletRequest request) {
        if (request.getUserPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = request.getUserPrincipal().getName();
        User user = userService.findUserByEmail(email);

        if (user.getShoppingCart() == null || user.getShoppingCart().getCartItems().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Order newOrder = orderService.saveOrderByUserAndReturn(user);
        if (newOrder == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        OrderDTO orderDTO = orderMapper.toDTO(newOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDTO);
    }

    @Operation(summary = "Accept an order (Admin)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order accepted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden (Admin only)"),
            @ApiResponse(responseCode = "400", description = "Invalid request (Missing 'accept' field or false)"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDTO> acceptOrder(@PathVariable long orderId, @RequestBody Map<String, Boolean> body,
            HttpServletRequest request) {

        // We check if the user is an admin
        if (!request.isUserInRole("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Only administators can accept orders.");
        }

        Boolean newState = body.get("accept");
        if (newState == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must send a JSON with the boolean field 'accept'");
        } else if (!newState) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Order has't been accepted because 'accept' field was false. ");
        }

        Optional<Order> op = orderService.findById(orderId);
        if (op.isPresent()) {
            Order order = op.get();
            orderService.acceptOrder(order);

            URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/orders/{orderId}")
                    .buildAndExpand(order.getOrderID())
                    .toUri();
            return ResponseEntity.created(location).body(orderMapper.toDTO(order));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "The order " + orderId + " does not exist.");
        }
    }
}