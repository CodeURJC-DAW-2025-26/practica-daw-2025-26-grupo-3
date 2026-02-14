package es.grupo3.practica25_26.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.grupo3.practica25_26.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
