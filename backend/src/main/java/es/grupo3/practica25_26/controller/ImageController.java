package es.grupo3.practica25_26.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.grupo3.practica25_26.service.ImageService;

@Controller
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/product-images/{id}")
    public ResponseEntity<Object> getImageFile(@PathVariable long id) throws SQLException {

        Resource imageFile = imageService.getMainImageByProductId(id);

        if (imageFile == null) {
            return ResponseEntity.notFound().build();
        }

        MediaType mediaType = MediaTypeFactory
                .getMediaType(imageFile)
                .orElse(MediaType.IMAGE_JPEG);

        return ResponseEntity
                .ok()
                .contentType(mediaType)
                .body(imageFile);
    }

    @GetMapping("/user-images/{id}")
    public ResponseEntity<Object> getUserImageFile(@PathVariable long id) throws SQLException {
        Resource imageFile = imageService.getImageFile(id);

        MediaType mediaType = MediaTypeFactory
                .getMediaType(imageFile)
                .orElse(MediaType.IMAGE_JPEG);

        return ResponseEntity
                .ok()
                .contentType(mediaType)
                .body(imageFile);
    }

}
