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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.HttpStatus;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import es.grupo3.practica25_26.dto.ImageDTO;
import es.grupo3.practica25_26.dto.UserBasicDTO;
import es.grupo3.practica25_26.dto.UserCreateDTO;
import es.grupo3.practica25_26.dto.UserPassBasicDTO;
import es.grupo3.practica25_26.dto.UserPassDTO;
import es.grupo3.practica25_26.dto.UserPostDTO;
import es.grupo3.practica25_26.mapper.ImageMapper;
import es.grupo3.practica25_26.mapper.UserBasicMapper;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.model.Error;
import es.grupo3.practica25_26.model.Image;
import es.grupo3.practica25_26.service.ImageService;
import es.grupo3.practica25_26.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private UserBasicMapper basicMapper;

        @Autowired
        private UserService userService;

        @Autowired
        private ImageService imageService;

        @Autowired
        private ImageMapper imageMapper;

        @Operation(summary = "Get all users (Admin)")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
                        @ApiResponse(responseCode = "403", description = "Forbidden (Admin only)")
        })
        @GetMapping
        public Collection<UserBasicDTO> getAllUsers(HttpServletRequest request) {
                if (!request.isUserInRole("ADMIN")) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                                        "Only administators can list all users.");
                }
                return basicMapper.toDTOs(userService.getAllUsers());
        }

        @GetMapping("/logged")
        public UserBasicDTO getLoggedUser(HttpServletRequest request) {
                String currentUserEmail = request.getUserPrincipal().getName();
                User currentUser = userService.findUserByEmail(currentUserEmail);

                return basicMapper.toDTO(currentUser);
        }

        @Operation(summary = "Get user by ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User found"),
                        @ApiResponse(responseCode = "404", description = "User not found")
        })
        @GetMapping("/{id}")
        public UserBasicDTO getUserById(@PathVariable long id) {
                User user = userService.findUserById(id);
                if (user == null) {
                        throw new NoSuchElementException("User not found");
                }
                return basicMapper.toDTO(user);
        }

        @Operation(summary = "Delete user by ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User deleted successfully"),
                        @ApiResponse(responseCode = "404", description = "User not found"),
                        @ApiResponse(responseCode = "403", description = "Forbidden (Permission denied)")
        })
        @DeleteMapping("/{id}")
        public UserBasicDTO deleteUserById(@PathVariable long id, @RequestBody UserPassBasicDTO currentPass,
                        HttpServletRequest request) {
                String email = request.getUserPrincipal().getName();
                User currentUser = userService.findUserByEmail(email);
                String password = currentPass.password();

                if (password == null || password.length() == 0) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "Es necesario introducir la contraseña de la cuenta antes de borrarla.");
                }
                if (!passwordEncoder.matches(password, currentUser.getPassword())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "La contraseña introducida es incorrecta.");
                }

                User deletedUser = userService.deleteUserById(id, email);
                return basicMapper.toDTO(deletedUser);
        }

        @Operation(summary = "Register new user")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "User registered successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid user data or email already in use")
        })
        @PostMapping
        public ResponseEntity<UserBasicDTO> createUser(@RequestBody UserCreateDTO newUserDTO) {
                User newUser = new User();
                newUser.setUserName(newUserDTO.userName());
                newUser.setSurname(newUserDTO.surname());
                newUser.setAddress(newUserDTO.address());
                newUser.setEmail(newUserDTO.email());
                newUser.setPassword(newUserDTO.password());
                newUser.setState(true);

                List<String> userRoles = new ArrayList<>();
                userRoles.add("USER");
                newUser.setRoles(userRoles);

                Error error = userService.userRegisterCheck(newUser);
                if (error != null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "There are errors in your user creation request: " + error.getTitle() + " "
                                                        + error.getMessage());
                }

                newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
                userService.saveUser(newUser);

                UserBasicDTO responseDTO = basicMapper.toDTO(newUser);
                URI location = fromCurrentRequest().path("/{id}").buildAndExpand(newUser.getId()).toUri();
                return ResponseEntity.created(location).body(responseDTO);
        }

        @Operation(summary = "Update user details")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User updated successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid user data or email conflict"),
                        @ApiResponse(responseCode = "404", description = "User not found")
        })
        @PutMapping("/{id}")
        public UserBasicDTO updateUser(@PathVariable long id, @RequestBody UserPostDTO updatedUserDTO,
                        HttpServletRequest request) {
                User updatedUser = userService.findUserById(id);
                if (updatedUser == null) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
                }

                updatedUser.setUserName(updatedUserDTO.userName());
                updatedUser.setSurname(updatedUserDTO.surname());
                updatedUser.setAddress(updatedUserDTO.address());
                updatedUser.setEmail(updatedUserDTO.email());

                Error error = userService.userUpdateApiCheck(updatedUserDTO, request);
                if (error != null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "There are errors in your user update request: " + error.getTitle() + " "
                                                        + error.getMessage());
                }

                return basicMapper
                                .toDTO(userService.replaceUser(id, updatedUser, request.getUserPrincipal().getName()));
        }

        // Change user password
        @PutMapping("/{id}/pass")
        public ResponseEntity<Map<String, String>> changePassword(@PathVariable long id,
                        @RequestBody UserPassDTO userPass,
                        HttpServletRequest request) {
                String email = request.getUserPrincipal().getName();
                User currentUser = userService.findUserByEmail(email);

                if (currentUser.getId() != id) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                        "You are not the owner of the account with id " + id
                                                        + " so you can't change the password");
                }

                Error error = userService.userPasswordUpdateCheck(userPass.newPass(), userPass.currentPass(), request);

                if (error != null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "There are errors in your password update request: " + error.getTitle() + " "
                                                        + error.getMessage());
                }

                currentUser.setPassword(passwordEncoder.encode(userPass.newPass()));
                userService.saveUser(currentUser);
                return ResponseEntity.ok(Map.of("message", "Password changed succesfully"));
        }

        // Upload a profile photo
        @Operation(summary = "Upload user profile image")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Image uploaded successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid image file")
        })
        @PostMapping("/{id}/image")
        public ResponseEntity<ImageDTO> uploadUserImage(@PathVariable long id,
                        @RequestParam("file") MultipartFile imageFile,
                        HttpServletRequest request) throws IOException {

                if (imageFile.isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "La imagen no puede estar vacía.");
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

        // Delete the current profile photo
        @Operation(summary = "Delete user profile image")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Image deleted successfully"),
                        @ApiResponse(responseCode = "404", description = "User or image not found"),
                        @ApiResponse(responseCode = "400", description = "User has no image to delete")
        })
        @DeleteMapping("/{id}/image/")
        public ImageDTO deleteUserImage(@PathVariable long id, HttpServletRequest request) {

                String loggedInEmail = request.getUserPrincipal().getName();

                User user = userService.findUserById(id);
                Image image = user.getImage();

                if (image == null) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                        "El usuario no tiene foto de perfil para borrar.");
                }

                userService.removeImageFromUser(id, loggedInEmail);
                return imageMapper.toDTO(image);
        }

        // Endpoint to change the state of the user (Blocked or Unblocked)
        // ONLY FOR ADMINS
        @Operation(summary = "Change user state (Admin)")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User state updated successfully"),
                        @ApiResponse(responseCode = "403", description = "Forbidden (Admin only)"),
                        @ApiResponse(responseCode = "404", description = "User not found"),
                        @ApiResponse(responseCode = "400", description = "Invalid request (Missing 'state' field)")
        })
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