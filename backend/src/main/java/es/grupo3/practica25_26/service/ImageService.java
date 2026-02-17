package es.grupo3.practica25_26.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import es.grupo3.practica25_26.model.Image;
import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.repository.ImageRepository;
import es.grupo3.practica25_26.repository.ProductRepository;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ProductRepository productRepository;

    public Image createImage(InputStream inputStream) throws IOException {

        Image image = new Image();

        try {
            image.setImageFile(new SerialBlob(inputStream.readAllBytes()));
        } catch (Exception e) {
            throw new IOException("Failed to create image", e);
        }

        return image;
    }

    public Resource getImageFile(long id) throws SQLException {

        Image image = imageRepository.findById(id).orElseThrow();

        if (image.getImageFile() != null) {
            return new InputStreamResource(image.getImageFile().getBinaryStream());
        } else {
            throw new RuntimeException("Image file not found");
        }
    }

    public Resource getMainImageByProductId(long productId) throws SQLException {

        Product product = productRepository.findById(productId).orElseThrow();

        if (product != null && !product.getImages().isEmpty()) {
            Image image = product.getImages().get(0); // we get the first image if it has images
            return new InputStreamResource(image.getImageFile().getBinaryStream());
        }

        return null;
    }

}
