package es.grupo3.practica25_26.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.grupo3.practica25_26.model.CartItem;
import es.grupo3.practica25_26.model.OrderItem;
import es.grupo3.practica25_26.model.Order;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private UserService userService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderRepository orderRepository;

    // Saves an order entity directly to the database
    public void save(Order order) {
        orderRepository.save(order);
    }

    // Retrieves all orders stored in the database
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    // Finds a specific order by its ID, returning an Optional to safely handle
    // nulls
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    // Deletes an order from the database based on its ID
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    // Handles the checkout process. Marked as Transactional to ensure data
    // integrity
    // (if the cart deletion fails, the order creation rolls back automatically).
    @Transactional
    public void saveOrderByUser(User user) {
        // Creates a new order for the user with the current date and a default state of
        // 1
        LocalDateTime localDate = java.time.LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        Order order = new Order(user, localDate.format(formatter), 1);

        // Retrieves items from the user's shopping cart
        List<CartItem> cartItems = user.getShoppingCart().getCartItems();
        List<OrderItem> orderItems = new ArrayList<>();

        // Converts each cart item into an order item and links it to the new order
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem(order, cartItem.getProduct(), cartItem.getQuantity());
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);

        // Sets the human-readable text for the order state based on its numeric value
        switch (order.getState()) {
            case 0:
                order.setStateText("Entregado");
                break;
            case 1:
                order.setStateText("Pendiente de entrega");
                break;
            case 2:
                order.setStateText("Pendiente de pago");
                break;
        }

        // Persists the new order in the database to generate its ID
        orderRepository.save(order);

        // Safely retrieves the user's existing list of orders
        List<Order> orderList = user.getOrders();
        if (orderList == null) {
            orderList = new ArrayList<>();
        }

        // Adds the newly created order to the user's history and updates the user
        // entity
        orderList.add(order);
        user.setOrders(orderList);
        userService.saveUser(user);

        // Empties the user's shopping cart after a successful checkout
        shoppingCartService.deleteShoppingCartByUser(user);
    }

    // Retrieves a list of orders that are either pending or revised, including
    // their associated users
    public List<Order> findPendingAndRevisedOrdersWithUser() {
        return orderRepository.findPendingAndRevisedOrdersWithUser();
    }

    public long countPendingOrders() {
        return orderRepository.countPendingAndRevisedOrders();
    }

    public double calculateTotalSalesAmount() {
        Double total = orderRepository.sumAllApprovedOrders();
        return total != null ? total : 0.0;
    }

    public List<Object[]> getTopSellingProducts(Pageable pageable) {
        return orderRepository.findTopSellingProducts(pageable);
    }
}