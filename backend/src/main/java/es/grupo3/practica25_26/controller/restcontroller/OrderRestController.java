package es.grupo3.practica25_26.controller.restcontroller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.grupo3.practica25_26.dto.OrderDTO;
import es.grupo3.practica25_26.mapper.OrderMapper;
import es.grupo3.practica25_26.model.Order;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.service.BillService;
import es.grupo3.practica25_26.service.OrderService;
import es.grupo3.practica25_26.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/{orderId}/bill")
    public ResponseEntity<byte[]> getOrderPdf(@PathVariable long orderId, HttpServletRequest request)
            throws IOException {
        String email = request.getUserPrincipal().getName();
        User user = userService.findUserByEmail(email);

        return billService.orderPdfConfig(orderId, user);
    }

    @GetMapping("/all/bill")
    public ResponseEntity<byte[]> getAllOrderPdf(HttpServletRequest request) throws IOException {
        String currentEmail = request.getUserPrincipal().getName();
        User currentUser = userService.findUserByEmail(currentEmail);
        return billService.allOrdersPdfConfig(currentUser);
    }

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
}
