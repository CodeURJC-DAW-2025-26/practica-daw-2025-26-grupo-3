package es.grupo3.practica25_26.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public void save(Product product) {
        productRepository.save(product);
    }

    public void saveAll(List<Product> products) {
        productRepository.saveAll(products);
    }

    public long count() {
        return productRepository.count();
    }

    public List<Product> findAll() {
        return productRepository.findAllWithImages();
    }

    // optional because the product with the given id may not exist
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

}
