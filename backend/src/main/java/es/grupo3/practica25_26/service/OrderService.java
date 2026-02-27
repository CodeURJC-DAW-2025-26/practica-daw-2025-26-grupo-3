package es.grupo3.practica25_26.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.grupo3.practica25_26.model.CartItem;
import es.grupo3.practica25_26.model.OrderItem;
import es.grupo3.practica25_26.model.Order;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.repository.OrderRepository;

@Service
public class OrderService {

    private final UserService userService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderRepository orderRepository;

    OrderService(UserService userService) {
        this.userService = userService;
    }

    public void save(Order order) {
        orderRepository.save(order);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    public void saveOrderByUser(User user) {
        Order order = new Order(user.getUserName(), java.time.LocalDateTime.now(), 1);
        List<CartItem> cartItems = user.getShoppingCart().getCartItems();
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem(order, cartItem.getProduct(), cartItem.getQuantity());
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);

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

        orderRepository.save(order);
        List<Order> orderList;

        orderList = user.getOrders();

        orderList.add(order);
        userService.saveUser(user);

        shoppingCartService.deleteShoppingCartByUser(user);
    }
}
