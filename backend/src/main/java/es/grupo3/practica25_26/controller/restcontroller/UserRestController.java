package es.grupo3.practica25_26.controller.restcontroller;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.HttpStatus;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import es.grupo3.practica25_26.dto.ImageDTO;
import es.grupo3.practica25_26.dto.UserBasicDTO;
import es.grupo3.practica25_26.dto.UserPostDTO;
import es.grupo3.practica25_26.mapper.ImageMapper;
import es.grupo3.practica25_26.mapper.UserBasicMapper;
import es.grupo3.practica25_26.mapper.UserPostMapper;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.model.Error;
import es.grupo3.practica25_26.model.Image;
import es.grupo3.practica25_26.service.ImageService;
import es.grupo3.practica25_26.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserBasicMapper basicMapper;

    @Autowired
    private UserPostMapper postMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ImageMapper imageMapper;

    @GetMapping
    public Collection<UserBasicDTO> getAllUsers(HttpServletRequest request) {
        if (!request.isUserInRole("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Only administators can list all users.");
        }
        return basicMapper.toDTOs(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public UserBasicDTO getUserById(@PathVariable long id) {
        User user = userService.findUserById(id);
        if (user == null) {
            throw new NoSuchElementException("User not found");
        }
        return basicMapper.toDTO(user);
    }

    @DeleteMapping("/{id}")
    public UserBasicDTO deleteUserById(@PathVariable long id, HttpServletRequest request) {
        User user = userService.deleteUserById(id, request.getUserPrincipal().getName());
        return basicMapper.toDTO(user);
    }

    @PostMapping
    public ResponseEntity<UserBasicDTO> createUser(@RequestBody UserPostDTO newUserDTO) {
        User newUser = postMapper.toDomain(newUserDTO);
        newUser.setState(true);

        List<String> userRoles = new ArrayList<>();
        userRoles.add("USER");
        newUser.setRoles(userRoles);

        Error error = userService.userRegisterCheck(newUser);
        if (error != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "There are errors in your user creation request: " + error.getTitle() + " " + error.getMessage());
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userService.saveUser(newUser);

        UserBasicDTO responseDTO = basicMapper.toDTO(newUser);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(newUser.getId()).toUri();
        return ResponseEntity.created(location).body(responseDTO);
    }

    @PutMapping("/{id}")
    public UserBasicDTO updateUser(@PathVariable long id, @RequestBody UserPostDTO updatedUserDTO,
            HttpServletRequest request) {
        User updatedUser = postMapper.toDomain(updatedUserDTO);
        String loggedEmail = request.getUserPrincipal().getName();
        User loggedUser = userService.findUserByEmail(loggedEmail);
        updatedUser.setRoles(loggedUser.getRoles());

        Error error = userService.userUpdateApiCheck(updatedUserDTO, request);
        if (error != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "There are errors in your user update request: " + error.getTitle() + " " + error.getMessage());
        }

        updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));

        return basicMapper.toDTO(userService.replaceUser(id, updatedUser, request.getUserPrincipal().getName()));
    }

    // Upload a profile photo
    @PostMapping("/{id}/image")
    public ResponseEntity<ImageDTO> uploadUserImage(@PathVariable long id, @RequestBody MultipartFile imageFile,
            HttpServletRequest request) throws IOException {

        if (imageFile.isEmpty()) {
            throw new IllegalArgumentException("Image file cannot be empty");
        }

        String loggedInEmail = request.getUserPrincipal().getName();
        Image image = imageService.createImage(imageFile.getInputStream());
        userService.addImageToUser(id, image, loggedInEmail);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/images/{imageId}")
                .buildAndExpand(image.getId())
                .toUri();

        return ResponseEntity.created(location).body(imageMapper.toDTO(image));
    }

    // Delete the actual profile photo
    @DeleteMapping("/{id}/image/")
    public ImageDTO deleteUserImage(@PathVariable long id, HttpServletRequest request) {

        String loggedInEmail = request.getUserPrincipal().getName();

        User user = userService.findUserById(id);
        Image image = user.getImage();

        if (image == null) {
            throw new IllegalArgumentException("El usuario no tiene foto de perfil para borrar");
        }

        userService.removeImageFromUser(id, loggedInEmail);
        return imageMapper.toDTO(image);
    }

    // Endpoint to change the state of the user (Blocked or Unblocked)
    // ONLY FOR ADMINS
    @PutMapping("/{id}/state")
    public UserBasicDTO changeUserState(@PathVariable long id, @RequestBody Map<String, Boolean> body,
            HttpServletRequest request) {

        // We check if the user is an admin
        if (!request.isUserInRole("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Only administrators can accept or block users.");
        }

        // We obtain the state of the user from the request (JSON)
        Boolean newState = body.get("state");
        if (newState == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must send a JSON with the boolean field 'state' ");
        }

        // This method verifies if the user state can be updated and in that case
        // updates it
        User updatedUser = userService.updateUserState(id, newState);

        return basicMapper.toDTO(updatedUser);
    }

}