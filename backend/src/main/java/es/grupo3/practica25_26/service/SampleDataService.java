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
        public void init() {
                try {
                        // 1. Inicializar Usuarios
                        User adminUser = getOrCreateAdmin("Admin", "System", "Admin Street 1, Spain",
                                        "admin@admin.com", "admin1234", true);

                        User exampleUser1 = getOrCreateUser("Marta", "Lopez", "Calle Mayor 12, Madrid",
                                        "marta@example.com", "demo1234", true);
                        User exampleUser2 = getOrCreateUser("Carlos", "Gomez", "Avenida Sol 7, Valencia",
                                        "carlos@example.com", "demo1234", true);

                        if (adminUser.getImage() == null) {
                                addImageToUser(adminUser, "static/images/commentor-item3.jpg");
                        }

                        if (exampleUser1.getImage() == null) {
                                addImageToUser(exampleUser1, "static/images/commentor-item2.jpg");
                        }

                        if (exampleUser2.getImage() == null) {
                                addImageToUser(exampleUser2, "static/images/commentor-item1.jpg");
                        }

                        // Check if products exist before adding them to avoid duplicates
                        if (productService.count() < 24) {
                                // Matrix with all example products: Name, Price, Condition (0=New, 1=Near New,
                                // 2=Used), Description
                                String[][] allProductsData = {
                                                { "Portatil Lenovo ThinkPad", "450.0", "2",
                                                                "Portatil usado en buen estado, 16GB RAM, 512GB SSD" },
                                                { "iPhone 12", "520.0", "1",
                                                                "Reacondicionado con bateria al 90% y garantia" },
                                                { "Auriculares Sony WH-1000XM4", "180.0", "1",
                                                                "Cancelacion de ruido y estuche incluido, casi nuevos" },
                                                { "Nintendo Switch", "210.0", "1",
                                                                "Impecable, muy poco uso. Incluye Joy-Con y dock" },
                                                { "Teclado mecanico Keychron K2", "75.0", "0",
                                                                "Nuevo a estrenar, precintado, interruptores brown" },
                                                { "Monitor LG 27\" 4K", "230.0", "2",
                                                                "Panel IPS, usado para teletrabajo, funciona perfectamente" },
                                                { "iPad Pro 11\"", "750.0", "1",
                                                                "Perfecto estado, 128GB, incluye cargador original" },
                                                { "Samsung Galaxy S22", "600.0", "1",
                                                                "Color negro, sin rasguños, como nuevo" },
                                                { "Apple Watch Series 7", "300.0", "2",
                                                                "Batería al 85%, ligeras marcas de uso en la esfera" },
                                                { "Kindle Paperwhite", "90.0", "0",
                                                                "Nuevo a estrenar, precintado en su caja original" },
                                                { "GoPro Hero 10", "250.0", "2",
                                                                "Usada en varios viajes de aventura, marcas estéticas pero lente intacta" },
                                                { "MacBook Air M1", "850.0", "1",
                                                                "Batería al 100%, 8GB RAM, 256GB SSD, cuidadísimo" },
                                                { "Ratón Logitech MX Master 3", "70.0", "0",
                                                                "Caja sellada sin abrir, ideal para productividad" },
                                                { "Auriculares inalámbricos Xiaomi Redmi Buds 4 Pro", "80.0", "0",
                                                                "Nuevos, cancelación de ruido, Bluetooth 5.3, estuche de carga incluido" },
                                                { "Tablet Samsung Galaxy Tab S8", "650.0", "1",
                                                                "Pantalla 11'', 128GB, S Pen incluido, poco uso" },
                                                { "Altavoz Bluetooth JBL Flip 5", "65.0", "2",
                                                                "Color azul, batería dura menos que de fábrica pero suena genial" },
                                                { "PlayStation 5", "450.0", "0",
                                                                "Nueva, con precinto de garantía, versión con lector de discos" },
                                                { "Xbox Series X", "420.0", "1",
                                                                "Abierta pero con poquísimo uso, caja y cables originales" },
                                                { "Mando DualSense PS5", "45.0", "2",
                                                                "Color blanco, stick izquierdo un poco desgastado por jugar al FIFA" },
                                                { "Cámara Canon EOS M50", "400.0", "1",
                                                                "Como nueva, incluye objetivo de kit 15-45mm" },
                                                { "Lente Sony 50mm f1.8", "150.0", "0",
                                                                "Nueva, nunca montada en cámara, comprada por error" },
                                                { "Micrófono Blue Yeti", "90.0", "2",
                                                                "Usado para podcast durante un año, sin soporte de mesa" },
                                                { "Tarjeta Gráfica RTX 3060", "280.0", "2",
                                                                "Usada solo para gaming los fines de semana, temperaturas excelentes" },
                                                { "Disco Duro Externo 2TB WD", "50.0", "0",
                                                                "Nuevo, empaquetado, USB 3.0" }

                                };

                                List<Product> productsToSave = new ArrayList<>();

                                // loop to create the objects
                                for (int i = 0; i < allProductsData.length; i++) {
                                        // We alternate the vendors: even numbers for Marta, odd numbers for Carlos.
                                        User seller = (i % 2 == 0) ? exampleUser1 : exampleUser2;

                                        Product product = new Product(
                                                        allProductsData[i][0],
                                                        Double.parseDouble(allProductsData[i][1]),
                                                        Integer.parseInt(allProductsData[i][2]),
                                                        allProductsData[i][3],
                                                        seller);
                                        productsToSave.add(product);
                                }

                                // Load images
                                try {

                                        // Product 1
                                        addImageToProduct(productsToSave.get(0), "static/images/lenovothinkpad1.jpg");
                                        addImageToProduct(productsToSave.get(0), "static/images/lenovothinkpad2.jpg");
                                        addImageToProduct(productsToSave.get(0), "static/images/lenovothinkpad3.jpg");

                                        // Product 2
                                        addImageToProduct(productsToSave.get(1), "static/images/iphone12_1.jpg");
                                        addImageToProduct(productsToSave.get(1), "static/images/iphone12_2.jpg");
                                        addImageToProduct(productsToSave.get(1), "static/images/iphone12_3.jpg");

                                        // Product 3
                                        addImageToProduct(productsToSave.get(2),
                                                        "static/images/2393-sony-wh1000xm5bce7-auriculares-inalambricos-con-cancelacion-de-ruido-negros-soft-case-comprar2.jpg");
                                        addImageToProduct(productsToSave.get(2),
                                                        "static/images/sony-wh1000xm5bce7-auriculares-inalambricos-con-cancelacion-de-ruido-negros-soft-case.jpg");
                                        addImageToProduct(productsToSave.get(2),
                                                        "static/images/4839-sony-wh1000xm5bce7-auriculares-inalambricos-con-cancelacion-de-ruido-negros-soft-case-estuche.jpg");

                                        // Product 4
                                        addImageToProduct(productsToSave.get(3), "static/images/nintendoSwitch1.jpg");
                                        addImageToProduct(productsToSave.get(3), "static/images/nintendoSwitch2.jpg");
                                        addImageToProduct(productsToSave.get(3), "static/images/nintendoswitch3.jpg");

                                        // Product 5
                                        addImageToProduct(productsToSave.get(4),
                                                        "static/images/Keychron-K2-wireless-mechanical-keyboard-for-Mac-Windows-iOS-Gateron-switch-red-with-type-C-RGB-white-backlight-aluminum-frame.jpg");
                                        addImageToProduct(productsToSave.get(4),
                                                        "static/images/Keychron-K2-wireless-mechanical-keyboard-for-Mac-Windows-iOS-Gateron-switch-red-with-type-C-RGB-white-backlight-exclusive-color.jpg");
                                        addImageToProduct(productsToSave.get(4),
                                                        "static/images/Keychron-K2-wireless-mechanical-keyboard-1.jpg");

                                        // Product 6
                                        addImageToProduct(productsToSave.get(5),
                                                        "static/images/1918-monitor-lg-27u730a-b-27-ultrahd-4k-60hz-ips-usb-c-altavoces-ajustable.jpg");
                                        addImageToProduct(productsToSave.get(5),
                                                        "static/images/2879-monitor-lg-27u730a-b-27-ultrahd-4k-60hz-ips-usb-c-altavoces-ajustable-comprar.jpg");
                                        addImageToProduct(productsToSave.get(5),
                                                        "static/images/8370-monitor-lg-27u730a-b-27-ultrahd-4k-60hz-ips-usb-c-altavoces-ajustable-foto.jpg");

                                        // Product 7
                                        addImageToProduct(productsToSave.get(6), "static/images/ipad11_PRO_1.png");
                                        addImageToProduct(productsToSave.get(6), "static/images/ipad11_PRO_2.png");
                                        addImageToProduct(productsToSave.get(6), "static/images/ipad11_PRO_3.png");

                                        // Product 8
                                        addImageToProduct(productsToSave.get(7),
                                                        "static/images/Samsung_galaxy_s22_1.png");
                                        addImageToProduct(productsToSave.get(7),
                                                        "static/images/Samsung_galaxy_s22_2.png");
                                        addImageToProduct(productsToSave.get(7),
                                                        "static/images/Samsung_galaxy_s22_3.png");

                                        // Product 9
                                        addImageToProduct(productsToSave.get(8),
                                                        "static/images/Apple_Watch_Series 7_1.png");
                                        addImageToProduct(productsToSave.get(8),
                                                        "static/images/Apple_Watch_Series 7_2.png");
                                        addImageToProduct(productsToSave.get(8),
                                                        "static/images/Apple_Watch_Series 7_3.png");

                                        // Product 10
                                        addImageToProduct(productsToSave.get(9),
                                                        "static/images/Kindle_Paperwhite_1.png");
                                        addImageToProduct(productsToSave.get(9),
                                                        "static/images/Kindle_Paperwhite_2.png");
                                        addImageToProduct(productsToSave.get(9),
                                                        "static/images/Kindle_Paperwhite_3.png");

                                        // Product 11
                                        addImageToProduct(productsToSave.get(10), "static/images/GoPro_Hero_10_1.png");
                                        addImageToProduct(productsToSave.get(10), "static/images/GoPro_Hero_10_2.png");
                                        addImageToProduct(productsToSave.get(10), "static/images/GoPro_Hero_10_3.png");

                                        // Product 12
                                        addImageToProduct(productsToSave.get(11), "static/images/MacBook_Air_M1_1.png");
                                        addImageToProduct(productsToSave.get(11), "static/images/MacBook_Air_M1_2.png");
                                        addImageToProduct(productsToSave.get(11), "static/images/MacBook_Air_M1_3.png");

                                        // Product 13
                                        addImageToProduct(productsToSave.get(12),
                                                        "static/images/Ratón_Logitech_MX_Master_3_1.png");
                                        addImageToProduct(productsToSave.get(12),
                                                        "static/images/Ratón_Logitech_MX_Master_3_2.png");
                                        addImageToProduct(productsToSave.get(12),
                                                        "static/images/Ratón_Logitech_MX_Master_3_3.png");

                                        // Product 14
                                        addImageToProduct(productsToSave.get(13),
                                                        "static/images/Auriculares_inalámbricos_Xiaomi_Redmi Buds_4_Pro_1.png");
                                        addImageToProduct(productsToSave.get(13),
                                                        "static/images/Auriculares_inalámbricos_Xiaomi_Redmi Buds_4_Pro_2.png");
                                        addImageToProduct(productsToSave.get(13),
                                                        "static/images/Auriculares_inalámbricos_Xiaomi_Redmi Buds_4_Pro_3.png");

                                        // Product 15
                                        addImageToProduct(productsToSave.get(14),
                                                        "static/images/Tablet_Samsung_Galaxy_Tab_S8_1.png");
                                        addImageToProduct(productsToSave.get(14),
                                                        "static/images/Tablet_Samsung_Galaxy_Tab_S8_2.png");
                                        addImageToProduct(productsToSave.get(14),
                                                        "static/images/Tablet_Samsung_Galaxy_Tab_S8_3.png");

                                        // Product 16
                                        addImageToProduct(productsToSave.get(15),
                                                        "static/images/Altavoz_Bluetooth_JBL_Flip_5_1.png");
                                        addImageToProduct(productsToSave.get(15),
                                                        "static/images/Altavoz_Bluetooth_JBL_Flip_5_2.png");
                                        addImageToProduct(productsToSave.get(15),
                                                        "static/images/Altavoz_Bluetooth_JBL_Flip_5_3.png");

                                        // Product 17
                                        addImageToProduct(productsToSave.get(16), "static/images/PlayStation_5_1.png");
                                        addImageToProduct(productsToSave.get(16), "static/images/PlayStation_5_2.png");
                                        addImageToProduct(productsToSave.get(16), "static/images/PlayStation_5_3.png");

                                        // Product 18
                                        addImageToProduct(productsToSave.get(17), "static/images/Xbox_Series_X_1.png");
                                        addImageToProduct(productsToSave.get(17), "static/images/Xbox_Series_X_2.png");
                                        addImageToProduct(productsToSave.get(17), "static/images/Xbox_Series_X_3.png");

                                        // Product 19
                                        addImageToProduct(productsToSave.get(18),
                                                        "static/images/Mando_DualSense_PS5_1.png");
                                        addImageToProduct(productsToSave.get(18),
                                                        "static/images/Mando_DualSense_PS5_2.png");
                                        addImageToProduct(productsToSave.get(18),
                                                        "static/images/Mando_DualSense_PS5_3.png");

                                        // Product 20
                                        addImageToProduct(productsToSave.get(19),
                                                        "static/images/Cámara_Canon_EOS_M50_1.png");
                                        addImageToProduct(productsToSave.get(19),
                                                        "static/images/Cámara_Canon_EOS_M50_2.png");
                                        addImageToProduct(productsToSave.get(19),
                                                        "static/images/Cámara_Canon_EOS_M50_3.png");

                                        // Product 21
                                        addImageToProduct(productsToSave.get(20),
                                                        "static/images/Lente_Sony_50mm_f1.8_1.png");
                                        addImageToProduct(productsToSave.get(20),
                                                        "static/images/Lente_Sony_50mm_f1.8_2.png");
                                        addImageToProduct(productsToSave.get(20),
                                                        "static/images/Lente_Sony_50mm_f1.8_3.png");

                                        // Product 22
                                        addImageToProduct(productsToSave.get(21),
                                                        "static/images/Micrófono_Blue_Yeti_1.png");
                                        addImageToProduct(productsToSave.get(21),
                                                        "static/images/Micrófono_Blue_Yeti_2.png");
                                        addImageToProduct(productsToSave.get(21),
                                                        "static/images/Micrófono_Blue_Yeti_3.png");

                                        // Product 23
                                        addImageToProduct(productsToSave.get(22),
                                                        "static/images/Tarjeta_Gráfica_RTX_3060_1.png");
                                        addImageToProduct(productsToSave.get(22),
                                                        "static/images/Tarjeta_Gráfica_RTX_3060_2.png");
                                        addImageToProduct(productsToSave.get(22),
                                                        "static/images/Tarjeta_Gráfica_RTX_3060_3.png");

                                        // Product 24
                                        addImageToProduct(productsToSave.get(23),
                                                        "static/images/Disco_Duro_Externo_2TB_WD_1.png");
                                        addImageToProduct(productsToSave.get(23),
                                                        "static/images/Disco_Duro_Externo_2TB_WD_2.png");
                                        addImageToProduct(productsToSave.get(23),
                                                        "static/images/Disco_Duro_Externo_2TB_WD_3.png");

                                } catch (Exception e) {
                                        System.out.println("Could not load sample images: " + e.getMessage());
                                }

                                // Save all the products in the ddbb
                                productService.saveAll(productsToSave);
                        }
                } catch (Exception e) {
                        System.out.println("Error initializing sample data: " + e.getMessage());
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

        private void addImageToUser(User user, String classpathResource) throws IOException {
                Resource image = new ClassPathResource(classpathResource);

                if (image.exists()) {
                        Image createdImage = imageService.createImage(image.getInputStream());
                        user.setImage(createdImage);
                        userService.saveUser(user);
                }
        }

        /**
         * Retrieves an existing user or creates a new one with the USER role.
         * Encodes the password using BCrypt before saving.
         */
        private User getOrCreateUser(String name, String surname, String address, String email, String password,
                        boolean state) {
                User user = userService.findUserByEmail(email);
                if (user != null) {
                        return user;
                }
                User newUser = new User(name, surname, address, email, passwordEncoder.encode(password));
                newUser.setRoles(new ArrayList<>(List.of("USER"))); // Assign default role
                newUser.setState(state); // Set user as active
                userService.saveUser(newUser);
                return newUser;
        }

        /**
         * Retrieves an existing admin or creates a new one with USER and ADMIN roles.
         */
        private User getOrCreateAdmin(String name, String surname, String address, String email, String password,
                        boolean state) {
                User user = userService.findUserByEmail(email);
                if (user != null) {
                        return user;
                }
                User newUser = new User(name, surname, address, email, passwordEncoder.encode(password));
                newUser.setRoles(new ArrayList<>(List.of("USER", "ADMIN"))); // Assign Admin privileges
                newUser.setState(state);
                userService.saveUser(newUser);
                return newUser;
        }
}