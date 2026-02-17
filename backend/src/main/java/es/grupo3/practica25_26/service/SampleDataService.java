package es.grupo3.practica25_26.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;

import es.grupo3.practica25_26.model.Image;
import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.model.User;
import jakarta.annotation.PostConstruct;

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
                User exampleUser1 = getOrCreateUser("Marta", "Lopez", "Calle Mayor 12, Madrid",
                                "marta@example.com", "demo1234");
                User exampleUser2 = getOrCreateUser("Carlos", "Gomez", "Avenida Sol 7, Valencia",
                                "carlos@example.com", "demo1234");

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

                        // Product 1
                        addImageToProduct(p1, "/sample_images/images/lenovothinkpad1.jpg");
                        addImageToProduct(p1, "/sample_images/images/lenovothinkpad2.jpg");
                        addImageToProduct(p1, "/sample_images/images/lenovothinkpad3.jpg");

                        // Product 2
                        addImageToProduct(p2, "/sample_images/images/iphone12_1.jpg");
                        addImageToProduct(p2, "/sample_images/images/iphone12_2.jpg");
                        addImageToProduct(p2, "/sample_images/images/iphone12_3.jpg");

                        // Product 3
                        addImageToProduct(p3,
                                        "/sample_images/images/2393-sony-wh1000xm5bce7-auriculares-inalambricos-con-cancelacion-de-ruido-negros-soft-case-comprar2.jpg");
                        addImageToProduct(p3,
                                        "/sample_images/images/sony-wh1000xm5bce7-auriculares-inalambricos-con-cancelacion-de-ruido-negros-soft-case.jpg");
                        addImageToProduct(p3,
                                        "/sample_images/images/4839-sony-wh1000xm5bce7-auriculares-inalambricos-con-cancelacion-de-ruido-negros-soft-case-estuche.jpg");
                        // Product 4
                        addImageToProduct(p4, "/sample_images/images/nintendoSwitch1.jpg");
                        addImageToProduct(p4, "/sample_images/images/nintendoSwitch2.jpg");
                        addImageToProduct(p4, "/sample_images/images/nintendoSwitch3.jpg");

                        // Product 5
                        addImageToProduct(p5,
                                        "/sample_images/images/Keychron-K2-wireless-mechanical-keyboard-for-Mac-Windows-iOS-Gateron-switch-red-with-type-C-RGB-white-backlight-aluminum-frame.jpg");
                        addImageToProduct(p5,
                                        "/sample_images/images/Keychron-K2-wireless-mechanical-keyboard-for-Mac-Windows-iOS-Gateron-switch-red-with-type-C-RGB-white-backlight-exclusive-color.jpg");
                        addImageToProduct(p5,
                                        "/sample_images/images/Keychron-K2-wireless-mechanical-keyboard-1.jpg");

                        // Product 6
                        addImageToProduct(p6,
                                        "/sample_images/images/1918-monitor-lg-27u730a-b-27-ultrahd-4k-60hz-ips-usb-c-altavoces-ajustable.jpg");
                        addImageToProduct(p6,
                                        "/sample_images/images/2879-monitor-lg-27u730a-b-27-ultrahd-4k-60hz-ips-usb-c-altavoces-ajustable-comprar.jpg");
                        addImageToProduct(p6,
                                        "/sample_images/images/8370-monitor-lg-27u730a-b-27-ultrahd-4k-60hz-ips-usb-c-altavoces-ajustable-foto.jpg");

                        // We save all the products at once
                        productService.saveAll(List.of(p1, p2, p3, p4, p5, p6));
                }
        }

        private void addImageToProduct(Product product, String classpathResource) throws IOException {

                Resource image = new ClassPathResource(classpathResource);

                Image createdImage = imageService.createImage(image.getInputStream());
                product.getImages().add(createdImage);

        }

        private User getOrCreateUser(String name, String surname, String address, String email, String password) {
                return userService.findUserByEmail(email).orElseGet(() -> {
                        User user = new User(name, surname, address, email, password);
                        userService.saveUser(user);
                        return user;
                });
        }

}
