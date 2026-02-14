package es.grupo3.practica25_26.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.grupo3.practica25_26.model.OrderItem;
import es.grupo3.practica25_26.repository.OrderItemRepository;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    public void save(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }

    public List<OrderItem> findAll() {
        return orderItemRepository.findAll();
    }

    public Optional<OrderItem> findById(Long id) {
        return orderItemRepository.findById(id);
    }

    public void deleteById(Long id) {
        orderItemRepository.deleteById(id);
    }
}
