package es.grupo3.practica25_26.controller.restcontroller;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.grupo3.practica25_26.dto.ImageDTO;
import es.grupo3.practica25_26.mapper.ImageMapper;
import es.grupo3.practica25_26.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/images")
public class ImageRestController {

        @Autowired
        private ImageService imageService;

        @Autowired
        private ImageMapper mapper;

        // gets the specific ImageDTO
        @Operation(summary = "Get image details")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Image details retrieved successfully"),
                        @ApiResponse(responseCode = "404", description = "Image not found")
        })
        @GetMapping("/{id}")
        public ImageDTO getImage(@PathVariable long id) {
                return mapper.toDTO(imageService.getImage(id));
        }

        // gets the file of the specifi image
        @Operation(summary = "Get image file content")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Image file retrieved successfully"),
                        @ApiResponse(responseCode = "404", description = "Image not found")
        })
        @GetMapping("/{id}/media")
        public ResponseEntity<Object> getImageFile(@PathVariable long id) throws SQLException {

                Resource imageFile = imageService.getImageFile(id);

                MediaType mediaType = MediaTypeFactory
                                .getMediaType(imageFile)
                                .orElse(MediaType.IMAGE_JPEG);

                return ResponseEntity.ok().contentType(mediaType).body(imageFile);

        }

        // Replace an image
        @Operation(summary = "Replace image file content")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Image replaced successfully"),
                        @ApiResponse(responseCode = "404", description = "Image not found"),
                        @ApiResponse(responseCode = "400", description = "Invalid image file")
        })
        @PutMapping("/{id}/media")
        public ResponseEntity<Object> replaceImageFile(@PathVariable long id, @RequestParam MultipartFile imageFile)
                        throws IOException {

                imageService.replaceImageFile(id, imageFile.getInputStream());

                return ResponseEntity.noContent().build();
        }

}
