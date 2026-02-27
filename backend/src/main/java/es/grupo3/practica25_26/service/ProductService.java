package es.grupo3.practica25_26.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.repository.CartItemRepository;
import es.grupo3.practica25_26.repository.OrderItemRepository;
import es.grupo3.practica25_26.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

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

    public boolean deleteProduct(Long id, String loggedInEmail, boolean isAdmin){

        
        Optional<Product> productOpt = productRepository.findById(id);

        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            User seller = product.getSeller();

            
            if (seller.getEmail().equals(loggedInEmail) || isAdmin) {
               
                // Delete the product from the seller
                if(seller.getProducts() != null){
                    seller.getProducts().remove(product);

                }
                
                //Delete the product from all of the shopping carts
                if (product.getCartItems()!=null){
                    cartItemRepository.deleteAll(product.getCartItems());
                    
                }

                //Delete the product from the orders 
                if (product.getOrderItems()!=null){
                    orderItemRepository.deleteAll(product.getOrderItems());
                }

                productRepository.deleteById(id);
                
                return true;
            }
          
        }
     return false;
    }

    /* 
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
        */
}
