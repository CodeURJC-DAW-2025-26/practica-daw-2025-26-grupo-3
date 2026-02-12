package es.grupo3.practica25_26.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import es.grupo3.practica25_26.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
}
