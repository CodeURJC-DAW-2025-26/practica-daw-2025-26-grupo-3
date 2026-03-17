package es.grupo3.practica25_26.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.grupo3.practica25_26.model.Image;
import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.repository.CartItemRepository;
import es.grupo3.practica25_26.repository.OrderItemRepository;
import es.grupo3.practica25_26.repository.ProductRepository;
import es.grupo3.practica25_26.model.Error;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserService userService;

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

    public List<Product> findTop8ByType(int favourite) {
        return productRepository.findTop8ByState(favourite);
    }

    public Product findById(long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void createNewProduct(Product product, List<MultipartFile> ProductImages, String loggedInEmail)
            throws IOException {

        // search of the seller
        User seller = userService.findUserByEmail(loggedInEmail);

        product.setSeller(seller);

        List<Image> productImages = new ArrayList<>();

        for (MultipartFile file : ProductImages) {

            if (!file.isEmpty()) {

                Image image = new Image();
                try {
                    image.setImageFile(new SerialBlob(file.getBytes()));
                } catch (Exception e) {
                    throw new IOException("Failed to create image", e);
                }

                productImages.add(image);
            }
        }

        // assign the images to the product
        product.setImages(productImages);

        productRepository.save(product);
    }

    public Product deleteProduct(Long id, String loggedInEmail, boolean isAdmin) {

        Product product = productRepository.findById(id).orElseThrow();

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

        }

        return product;
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
                    // save the product in ddbb
                    productRepository.save(existingProduct);
                }
            }
        }
    }

    public Error productCreateCheck(Product product, List<MultipartFile> images) {

        Error error = null;

        // Verification of the product name
        error = productNameCheck(product.getProductName());
        if (error != null)
            return error;

        // Verification of the product state
        error = productStateCheck(product.getState());
        if (error != null)
            return error;

        // Verification of de product price
        error = productPriceCheck(product.getPrice());
        if (error != null)
            return error;

        // Verification of the product description
        error = productDescriptionCheck(product.getDescription());
        if (error != null)
            return error;

        // Verification of the product images
        error = productImagesCheck(images);
        if (error != null)
            return error;

        return error;
    }

    public Error productUpdateCheck(Product editedProduct, Product existingProduct,
            List<Long> removeImages, List<MultipartFile> newImages) {

        Error error = null;

        // Name verification
        error = productNameCheck(editedProduct.getProductName());
        if (error != null)
            return error;

        // Price verification
        error = productPriceCheck(editedProduct.getPrice());
        if (error != null)
            return error;

        // Description verification
        error = productDescriptionCheck(editedProduct.getDescription());
        if (error != null)
            return error;

        error = productUpdateImagesCheck(existingProduct, removeImages, newImages);
        if (error != null)
            return error;

        return error;
    }

    // **** FORMS FIELD VALIDATION METHODS ****

    public Error productStateCheck(Integer state) {

        if (state == null || state < 0 || state > 2) {
            return new Error("¡Estado del producto inválido!",
                    "Por favor, selecciona un estado válido para el producto en el desplegable.");
        }
        return null;
    }

    public Error productNameCheck(String productName) {
        // If the product name is empty (trim deletes the blank spaces from the space
        // bar)
        if (productName == null || productName.trim().isEmpty()) {

            return new Error("¡Nombre vacío!",
                    "El nombre del producto no puede estar vacío.");

        }
        if (productName.length() < 3 || productName.length() > 100) {
            return new Error("¡Longitud de nombre incorrecta!",
                    "El nombre debe tener entre 3 y 100 caracteres. Has introducido " + productName.length()
                            + " caracteres.");

        }
        // if there isn´t any error, error is null
        return null;
    }

    public Error productPriceCheck(Double price) {

        // price lower than 1 euro
        if (price < 1) {
            return new Error("¡Precio inválido!",
                    "El precio mínimo de venta es de 1€. Has introducido " + price + "€.");
        }
        return null;
    }

    public Error productDescriptionCheck(String description) {

        if (description == null || description.trim().isEmpty()) {
            return new Error("¡Descripción vacía!",
                    "La descripción del producto no puede estar vacía.");

        }

        if (description.length() < 15 || description.length() > 1000) {
            return new Error("¡Longitud de descripción incorrecta!",
                    "La descripción debe tener entre 15 y 1000 caracteres. Has introducido " + description.length()
                            + " caracteres.");
        }
        return null;
    }

    public Error productImagesCheck(List<MultipartFile> images) {
        if (images == null || images.isEmpty() || images.get(0).isEmpty()) {
            return new Error("¡No existe ninguna imagen del producto!",
                    "Debes subir al menos una imagen de tu producto.");

        }
        return null;
    }

    public Error productUpdateImagesCheck(Product existingProduct, List<Long> removeImages,
            List<MultipartFile> newImages) {

        // number of photos the product has in ddbb
        int currentImages = existingProduct.getImages().size();

        // number of photos marked to be deleted
        int imagesToRemove = (removeImages != null) ? removeImages.size() : 0;

        // are there any new photos?
        boolean hasNewImages = false;
        if (newImages != null && !newImages.isEmpty() && !newImages.get(0).isEmpty()) {
            hasNewImages = true;
        }

        // We do the calculation, if the user deletes all images and doesn´t upload new
        // ones, we return a error
        if ((currentImages - imagesToRemove) <= 0 && !hasNewImages) {
            return new Error("¡Te quedas sin imágenes!",
                    "No puedes eliminar todas las fotos del producto sin subir al menos una nueva.");
        }
        return null;
    }

    public Page<Product> getProductsPage(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public long countTotalProducts() {
        return productRepository.count();
    }
}