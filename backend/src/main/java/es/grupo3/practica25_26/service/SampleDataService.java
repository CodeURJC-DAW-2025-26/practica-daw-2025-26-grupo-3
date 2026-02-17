package es.grupo3.practica25_26.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;

import es.grupo3.practica25_26.model.Image;
import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.model.Role;
import es.grupo3.practica25_26.model.User;
import jakarta.annotation.PostConstruct;

/**
 * Service to create sample data for the application, such as users, products
 * and images,
 * when the application starts. This is useful for testing and development
 * purposes,
 * as it allows us to have some initial data to work with without having to
 * manually create
 * it through the application's user interface.
 */
@Service
public class SampleDataService {

        @Autowired
        private ProductService productService;

        @Autowired
        private UserService userService;

        @Autowired
        private ImageService imageService;

        @PostConstruct
        public void init() throws IOException {
                // Create or retrieve admin user first
                User adminUser = getOrCreateAdmin("Admin", "System", "Admin Street 1, Spain",
                                "admin@admin.com", "12345678");
                if (adminUser.getImage() == null) {
                        addImageToUser(adminUser, "/sample_images/images/profile.png");
                }

                // Create or retrieve regular users
                User exampleUser1 = getOrCreateUser("Marta", "Lopez", "Calle Mayor 12, Madrid",
                                "marta@example.com", "demo1234");
                if (exampleUser1.getImage() == null) {
                        addImageToUser(exampleUser1, "/sample_images/images/commentor-item2.jpg");
                }

                User exampleUser2 = getOrCreateUser("Carlos", "Gomez", "Avenida Sol 7, Valencia",
                                "carlos@example.com", "demo1234");
                if (exampleUser2.getImage() == null) {
                        addImageToUser(exampleUser2, "/sample_images/images/commentor-item1.jpg");
                }

                // EXAMPLE PRODUCTS

                if (productService.count() == 0) {
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

                        productService.saveAll(List.of(p1, p2, p3, p4, p5, p6));

                        addImageToProduct(p1, "/sample_images/images/commentor-item1.jpg");
                        addImageToProduct(p1, "/sample_images/images/admin_panel.png"); // Segunda imagen

                        addImageToProduct(p2, "/sample_images/images/commentor-item2.jpg");
                        addImageToProduct(p3, "/sample_images/images/commentor-item3.jpg");

                        addImageToProduct(p4, "/sample_images/images/commentor-item1.jpg");
                        addImageToProduct(p4, "/sample_images/images/commentor-item2.jpg");

                        addImageToProduct(p5, "/sample_images/images/commentor-item2.jpg");
                        addImageToProduct(p6, "/sample_images/images/commentor-item3.jpg");

                        // We save all the products at once
                        productService.saveAll(List.of(p1, p2, p3, p4, p5, p6));
                }
        }

        private void addImageToProduct(Product product, String classpathResource) throws IOException {

                Resource image = new ClassPathResource(classpathResource);

                Image createdImage = imageService.createImage(image.getInputStream());
                product.getImages().add(createdImage);

        }

        private void addImageToUser(User user, String classpathResource) throws IOException {
                Resource image = new ClassPathResource(classpathResource);

                Image createdImage = imageService.createImage(image.getInputStream());
                user.setImage(createdImage);
                userService.saveUser(user);
        }

        private User getOrCreateUser(String name, String surname, String address, String email, String password) {
                return userService.findUserByEmail(email).orElseGet(() -> {
                        User user = new User(name, surname, address, email, password);
                        userService.saveUser(user);
                        return user;
                });
        }

        // Helper method to create or retrieve an admin user with ADMIN role
        private User getOrCreateAdmin(String name, String surname, String address, String email, String password) {
                return userService.findUserByEmail(email).orElseGet(() -> {
                        User adminUser = new User(name, surname, address, email, password, Role.ADMIN);
                        userService.saveUser(adminUser);
                        return adminUser;
                });

        }
}