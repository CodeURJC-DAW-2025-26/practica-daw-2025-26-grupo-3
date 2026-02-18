package es.grupo3.practica25_26.service;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList; // Necessary for Lists

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder; // Valid import now

import es.grupo3.practica25_26.model.Image;
import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.model.User;
import jakarta.annotation.PostConstruct;

/**
 * Service responsible for populating the database with sample data
 * (Users, Products, Images) upon application startup.
 * This ensures the application always has data for testing purposes
 * without needing manual entry.
 */
@Service
public class SampleDataService {

        @Autowired
        private ProductService productService;

        @Autowired
        private UserService userService;

        @Autowired
        private ImageService imageService;

        @Autowired
        private PasswordEncoder passwordEncoder; // Injected dependency for password hashing

        /**
         * This method runs automatically after the bean is initialized.
         * It checks if the database is empty and adds sample data if needed.
         */
        @PostConstruct
        public void init() throws IOException {

                // 1. Initialize Users (Admin and Standard)
                // We pass raw passwords here ("12345678"); they will be encoded in the helper
                // methods.

                User adminUser = getOrCreateAdmin("Admin", "System", "Admin Street 1, Spain",
                                "admin@admin.com", "12345678");

                User exampleUser1 = getOrCreateUser("Marta", "Lopez", "Calle Mayor 12, Madrid",
                                "marta@example.com", "demo1234");

                User exampleUser2 = getOrCreateUser("Carlos", "Gomez", "Avenida Sol 7, Valencia",
                                "carlos@example.com", "demo1234");

                // 2. Initialize Products
                // We only create products if the database is empty to prevent duplicates on
                // restart.
                if (productService.count() == 0) {

                        // Creating product instances linked to the users created above
                        Product p1 = new Product("Portatil Lenovo ThinkPad", 450.0, 2,
                                        "Portatil usado en buen estado, 16GB RAM, 512GB SSD", exampleUser1);
                        Product p2 = new Product("iPhone 12", 520.0, 1,
                                        "Reacondicionado con bateria al 90% y garantia", exampleUser1);
                        Product p3 = new Product("Auriculares Sony WH-1000XM4", 180.0, 1,
                                        "Cancelacion de ruido y estuche incluido", exampleUser1);

                        Product p4 = new Product("Nintendo Switch", 210.0, 2,
                                        "Incluye Joy-Con y dock, con caja", exampleUser2);
                        Product p5 = new Product("Teclado mecanico Keychron K2", 75.0, 0,
                                        "Nuevo, interruptores brown, layout ES", exampleUser2);
                        Product p6 = new Product("Monitor LG 27\" 4K", 230.0, 2,
                                        "Panel IPS, sin pixeles muertos", exampleUser2);

                        // Save products first to generate their IDs in the database
                        productService.saveAll(List.of(p1, p2, p3, p4, p5, p6));

                        // 3. Load and assign images from the classpath resources
                        // Using a try-catch block to prevent crash if images are missing
                        try {
                                // IMPORTANT: Ensure these folders exist in src/main/resources
                                addImageToProduct(p1, "/static/images/laptop.jpg");
                                addImageToProduct(p1, "/static/images/admin_panel.png");

                                // You can uncomment these lines once you have the actual image files
                                // addImageToProduct(p2, "/static/images/iphone.jpg");
                                // addImageToProduct(p3, "/static/images/sony.jpg");

                        } catch (Exception e) {
                                System.out.println(
                                                "Could not load sample images (Checked exception): " + e.getMessage());
                        }

                        // Update products to save the new image relationships
                        productService.saveAll(List.of(p1, p2, p3, p4, p5, p6));
                }
        }

        /**
         * Helper method to load an image from src/main/resources and attach it to a
         * product.
         */
        private void addImageToProduct(Product product, String classpathResource) throws IOException {
                Resource image = new ClassPathResource(classpathResource);
                if (image.exists()) {
                        Image createdImage = imageService.createImage(image.getInputStream());
                        product.getImages().add(createdImage);
                }
        }

        /**
         * Retrieves an existing user or creates a new one with the USER role.
         * Encodes the password using BCrypt before saving.
         */
        private User getOrCreateUser(String name, String surname, String address, String email, String password) {
                return userService.findUserByEmail(email).orElseGet(() -> {
                        User user = new User(name, surname, address, email, passwordEncoder.encode(password));
                        user.setRoles(new ArrayList<>(List.of("USER"))); // Assign default role
                        userService.saveUser(user);
                        return user;
                });
        }

        /**
         * Retrieves an existing admin or creates a new one with USER and ADMIN roles.
         */
        private User getOrCreateAdmin(String name, String surname, String address, String email, String password) {
                return userService.findUserByEmail(email).orElseGet(() -> {
                        User user = new User(name, surname, address, email, passwordEncoder.encode(password));
                        user.setRoles(new ArrayList<>(List.of("USER", "ADMIN"))); // Assign Admin privileges
                        userService.saveUser(user);
                        return user;
                });
        }
}