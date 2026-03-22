package es.grupo3.practica25_26.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletRequest;

import es.grupo3.practica25_26.model.Order;
import es.grupo3.practica25_26.model.User;

import es.grupo3.practica25_26.service.ErrorService;
import es.grupo3.practica25_26.service.OrderService;
import es.grupo3.practica25_26.service.UserService;
import es.grupo3.practica25_26.service.BillService;

@Controller
public class OrderController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ErrorService errorService;

    @Autowired
    private BillService billService;

    // Converts one user order to PDF
    @GetMapping("/bill/{id}")
    public ResponseEntity<byte[]> exportOrderToPDF(@PathVariable long id, HttpServletRequest request)
            throws IOException {
        String email = request.getUserPrincipal().getName();
        User user = userService.findUserByEmail(email);

        return billService.orderPdfConfig(id, user);
    }

    // Converts all the user's orders to PDF
    @GetMapping("/bill/all")
    public ResponseEntity<byte[]> exportAllOrderToPDF(HttpServletRequest request) throws IOException {
        String email = request.getUserPrincipal().getName();
        User currentUser = userService.findUserByEmail(email);

        return billService.allOrdersPdfConfig(currentUser);
    }

    // Fetches and displays a list of orders that are either pending or revised for
    // admin review
    @GetMapping("/orders_list")
    public String ordersList(Model model) {
        model.addAttribute("orders", orderService.findPendingAndRevisedOrdersWithUser()); // 1 state pending
        return "orders_list";
    }

    // Handles the admin action to accept an order, marking it as Delivered (0)
    @PostMapping("/orders/{id}/accept")
    public String acceptOrder(Model model, @PathVariable long id) {
        Optional<Order> op = orderService.findById(id);

        if (op.isPresent()) {
            orderService.acceptOrder(op.get());
            return "redirect:/orders_list"; // Changed to orders_list where this button is located
        } else {
            return errorService.setErrorPageWithButton(model, null, "Pedido no encontrado",
                    "El pedido que intentas aceptar no existe", "Volver a pedidos", "/orders_list");
        }
    }
}