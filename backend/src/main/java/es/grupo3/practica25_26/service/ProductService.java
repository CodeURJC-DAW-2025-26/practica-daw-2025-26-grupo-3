package es.grupo3.practica25_26.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.grupo3.practica25_26.model.Image;
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

    public boolean deleteProduct(Long id, String loggedInEmail, boolean isAdmin) {

        Optional<Product> productOpt = productRepository.findById(id);

        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            User seller = product.getSeller();

            if (seller.getEmail().equals(loggedInEmail) || isAdmin) {

                // Delete the product from the seller
                if (seller.getProducts() != null) {
                    seller.getProducts().remove(product);

                }

                // Delete the product from all of the shopping carts
                if (product.getCartItems() != null) {
                    cartItemRepository.deleteAll(product.getCartItems());

                }

                // Delete the product from the orders
                if (product.getOrderItems() != null) {
                    orderItemRepository.deleteAll(product.getOrderItems());
                }

                productRepository.deleteById(id);

                return true;
            }

        }
        return false;
    }

    public void updateProduct(Long id, Product editedProduct, List<Long> removeImages, List<MultipartFile> newImages,
            String loggedInEmail, boolean isAdmin) throws IOException {

        Optional<Product> productOpt = productRepository.findById(id);

        if (productOpt.isPresent()) {
            Product existingProduct = productOpt.get();
            User seller = existingProduct.getSeller();

            // Comprobation if the user is an admin or the owner of the product
            if (seller.getEmail().equals(loggedInEmail) || isAdmin) {

                existingProduct.setProductName(editedProduct.getProductName());
                existingProduct.setPrice(editedProduct.getPrice());
                existingProduct.setState(editedProduct.getState());
                existingProduct.setDescription(editedProduct.getDescription());

                // Remove the images selected
                if (removeImages != null && !removeImages.isEmpty()) {
                    List<Image> images = existingProduct.getImages();

                    // Inverse for becouse when a image is deleted, the list is resized
                    for (int i = images.size() - 1; i >= 0; i--) {

                        Image img = images.get(i);

                        // If the image is selected to be deleted
                        if (removeImages.contains(img.getId())) {
                            images.remove(i);
                        }
                    }

                }

                
                // Add the new images to the product if there are new images
                if (newImages != null) {
                for (MultipartFile file : newImages) {

                    if (!file.isEmpty()) {

                        Image image = new Image();
                        try {
                            image.setImageFile(new SerialBlob(file.getBytes()));
                        } catch (Exception e) {
                            throw new IOException("Failed to create image", e);
                        }
                        
                        // assign the new images to the product
                        existingProduct.getImages().add(image);

                    }

                }
                //save the product in ddbb
                productRepository.save(existingProduct);
                
            }
            }

        }

    }

    /*
     * public void deleteById(Long id) {
     * productRepository.deleteById(id);
     * }
     */
}
