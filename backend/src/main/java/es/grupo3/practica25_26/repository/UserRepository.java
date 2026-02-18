package es.grupo3.practica25_26.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.grupo3.practica25_26.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query
    Optional<User> findByEmailAndPassword(String email, String password);

    @Query
    Optional<User> findDistinctByEmail(String email);

    @Query
    Optional<User> findDistinctById(long id);
}
