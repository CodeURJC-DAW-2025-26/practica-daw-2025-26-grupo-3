package es.grupo3.practica25_26.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.grupo3.practica25_26.model.Image;
import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.repository.ImageRepository;
import es.grupo3.practica25_26.repository.ProductRepository;
import es.grupo3.practica25_26.repository.UserRepository;
import jakarta.annotation.PostConstruct;

@Service
public class SampleDataService {

        @Autowired
        private ProductRepository productRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private ImageRepository imageRepository;

        @PostConstruct
        public void init() {
                if (userRepository.count() > 0) {
                        return;
                }

                User exampleUser1 = new User("Marta", "Lopez", "Calle Mayor 12, Madrid", "marta@example.com",
                                "demo1234");
                User exampleUser2 = new User("Carlos", "Gomez", "Avenida Sol 7, Valencia", "carlos@example.com",
                                "demo1234");
                User exampleUser3 = new User("Juan", "Cuesta", "Calle Desengaño 21, Madrid", "juan@example.com",
                                "demo1234");
                User exampleUser4 = new User("Pedro", "Ramirez", "Calle Tetuan 5, Sevilla", "pedro@example.com",
                                "demo1234");
                User exampleUser5 = new User("Ana", "Fernandez", "Avenida Diagonal 200, Barcelona",
                                "ana@example.com", "demo1234");
                User exampleUser6 = new User("Luis", "García", "Plaza España 1, Zaragoza", "luis@example.com",
                                "demo1234");
                User exampleUser7 = new User("Maria", "Sanchez", "Gran Vía 33, Bilbao", "maria@example.com",
                                "demo1234");
                User exampleUser8 = new User("Sofia", "Ruiz", "Calle Larios 10, Málaga", "sofia@example.com",
                                "demo1234");
                User exampleUser9 = new User("Javier", "Torres", "Paseo de la Castellana 100, Madrid",
                                "javier@example.com", "demo1234");
                User exampleUser10 = new User("Lucia", "Martin", "Calle San Vicente 4, Valencia",
                                "lucia@example.com", "demo1234");
                User exampleUser11 = new User("David", "Moreno", "Rúa do Franco 15, Santiago de Compostela",
                                "david@example.com", "demo1234");
                User exampleUser12 = new User("Elena", "Jimenez", "Calle Santa María 2, Toledo",
                                "elena@example.com", "demo1234");
                User exampleUser13 = new User("Pablo", "Diaz", "Calle Corrida 8, Gijón", "pablo@example.com",
                                "demo1234");

                userRepository.saveAll(List.of(exampleUser1, exampleUser2, exampleUser3, exampleUser4, exampleUser5,
                                exampleUser6, exampleUser7, exampleUser8, exampleUser9, exampleUser10,
                                exampleUser11,
                                exampleUser12, exampleUser13));

                // EXAMPLE PRODUCTS

                if (productRepository.count() > 0) {
                        return;
                }
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

                productRepository.saveAll(List.of(p1, p2, p3, p4, p5, p6));

                // Cargar imágenes específicas para cada producto desde static/images
                loadImageForProduct(p1, "commentor-item1.jpg");
                loadImageForProduct(p2, "commentor-item2.jpg");
                loadImageForProduct(p3, "commentor-item3.jpg");
                loadImageForProduct(p4, "commentor-item1.jpg");
                loadImageForProduct(p5, "commentor-item2.jpg");
                loadImageForProduct(p6, "commentor-item3.jpg");
        }

        private void loadImageForProduct(Product product, String imageName) {

                String imagePath = "src/main/resources/sample_images/images/" + imageName;
                Path path = Paths.get(imagePath);

                if (!Files.exists(path)) {
                        System.out.println(" Imagen no encontrada: " + imagePath);
                        return;
                }

                try {
                        byte[] imageBytes = Files.readAllBytes(path);
                        Image image = new Image(new SerialBlob(imageBytes));
                        imageRepository.save(image);
                        product.getImages().add(image);
                        productRepository.save(product);
                        System.out.println(" Imagen cargada: " + imageName);
                } catch (IOException | SQLException e) {
                        System.err.println(" Error cargando imagen: " + imageName);
                        e.printStackTrace();
                }
        }

}
