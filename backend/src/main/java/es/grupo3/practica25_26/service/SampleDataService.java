package es.grupo3.practica25_26.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.repository.ProductRepository;
import es.grupo3.practica25_26.repository.UserRepository;
import jakarta.annotation.PostConstruct;

@Service
public class SampleDataService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        if (userRepository.count() > 0) {
            return;
        }

        User sellerOne = new User();
        sellerOne.setUserName("Marta");
        sellerOne.setSurname("Lopez");
        sellerOne.setAddress("Calle Mayor 12, Madrid");
        sellerOne.setEmail("marta@example.com");
        sellerOne.setPassword("demo1234");

        User sellerTwo = new User();
        sellerTwo.setUserName("Carlos");
        sellerTwo.setSurname("Gomez");
        sellerTwo.setAddress("Avenida Sol 7, Valencia");
        sellerTwo.setEmail("carlos@example.com");
        sellerTwo.setPassword("demo1234");

        userRepository.saveAll(List.of(sellerOne, sellerTwo));

        Product p1 = new Product("Portatil Lenovo ThinkPad", 450.0, 2,
                "Portatil usado en buen estado, 16GB RAM, 512GB SSD", sellerOne);
        Product p2 = new Product("iPhone 12", 520.0, 1,
                "Reacondicionado con bateria al 90% y garantia", sellerOne);
        Product p3 = new Product("Auriculares Sony WH-1000XM4", 180.0, 1,
                "Cancelacion de ruido y estuche incluido", sellerOne);

        Product p4 = new Product("Nintendo Switch", 210.0, 2,
                "Incluye Joy-Con y dock, con caja", sellerTwo);
        Product p5 = new Product("Teclado mecanico Keychron K2", 75.0, 0,
                "Nuevo, interruptores brown, layout ES", sellerTwo);
        Product p6 = new Product("Monitor LG 27\" 4K", 230.0, 2,
                "Panel IPS, sin pixeles muertos", sellerTwo);

        productRepository.saveAll(List.of(p1, p2, p3, p4, p5, p6));
    }

}
