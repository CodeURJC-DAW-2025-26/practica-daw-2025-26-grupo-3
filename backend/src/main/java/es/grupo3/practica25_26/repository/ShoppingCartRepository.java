package es.grupo3.practica25_26.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.grupo3.practica25_26.model.ShoppingCart;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

}
