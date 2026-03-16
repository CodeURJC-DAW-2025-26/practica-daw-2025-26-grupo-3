package es.grupo3.practica25_26.restcontroller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import es.grupo3.practica25_26.dto.UserBasicDTO;
import es.grupo3.practica25_26.dto.UserPostDTO;
import es.grupo3.practica25_26.mapper.UserBasicMapper;
import es.grupo3.practica25_26.mapper.UserPostMapper;
import es.grupo3.practica25_26.model.User;
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

    @GetMapping("/")
    public Collection<UserBasicDTO> getAllUsers() {
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

    @PostMapping("/")
    public ResponseEntity<UserBasicDTO> createUser(@RequestBody UserPostDTO newUserDTO) {
        User newUser = postMapper.toDomain(newUserDTO);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        List<String> userRoles = new ArrayList<>();
        userRoles.add("USER");
        newUser.setRoles(userRoles);

        userService.saveUser(newUser);

        UserBasicDTO responseDTO = basicMapper.toDTO(newUser);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(newUser.getId()).toUri();
        return ResponseEntity.created(location).body(responseDTO);
    }

    @PutMapping("/{id}")
    public UserBasicDTO updateUser(@PathVariable long id, @RequestBody UserPostDTO updatedUserDTO,
            HttpServletRequest request) {
        User updatedUser = postMapper.toDomain(updatedUserDTO);

        List<String> userRoles = new ArrayList<>();
        userRoles.add("USER");
        updatedUser.setRoles(userRoles);

        updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));

        return basicMapper.toDTO(userService.replaceUser(id, updatedUser, request.getUserPrincipal().getName()));
    }
}