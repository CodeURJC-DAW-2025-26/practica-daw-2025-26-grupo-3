package es.grupo3.practica25_26.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import es.grupo3.practica25_26.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> findDistinctByEmail(String email);

    Optional<User> findDistinctById(long id);

    // Query to find only the users
    List<User> findDistinctByRoles(String role);

    // Query to find users that do not have ADMIN role
    @Query("SELECT u FROM User u WHERE :role NOT IN elements(u.roles)")
    List<User> findUsersWithoutRole(@Param("role") String role);
}
