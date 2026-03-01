package es.grupo3.practica25_26.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.grupo3.practica25_26.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Custom JPQL query to fetch orders with state 1 (pending) or 2
    // (revised/pending payment).
    // Uses JOIN FETCH to eagerly load the associated User and their Image in a
    // single database hit,
    // avoiding the N+1 query performance issue. LEFT JOIN is used for the image in
    // case a user doesn't have one.
    @Query("SELECT o FROM Order o JOIN FETCH o.user u LEFT JOIN FETCH u.image WHERE o.state IN (1, 2)")
    List<Order> findPendingAndRevisedOrdersWithUser();

    @Query("SELECT COUNT(o) FROM Order o WHERE o.state IN (1, 2)")
    long countPendingAndRevisedOrders();

    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.state = 0")
    Double sumAllApprovedOrders();
}