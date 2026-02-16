package es.grupo3.practica25_26.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.grupo3.practica25_26.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("select distinct p from Product p left join fetch p.images")
	List<Product> findAllWithImages();

}
