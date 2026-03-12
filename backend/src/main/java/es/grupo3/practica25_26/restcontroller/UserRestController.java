package es.grupo3.practica25_26.restcontroller;

import java.util.Collection;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.grupo3.practica25_26.dto.UserBasicDTO;
import es.grupo3.practica25_26.mapper.UserBasicMapper;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.service.UserService;

@RestController
@RequestMapping("/api/v1/user")
public class UserRestController {

    @Autowired
    private UserBasicMapper mapper;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public Collection<UserBasicDTO> getAllUsers() {
        return mapper.toDTOs(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public UserBasicDTO getUserById(@PathVariable long id) {
        User user = userService.findUserById(id);
        if (user == null) {
            throw new NoSuchElementException("User not found");
        }
        return mapper.toDTO(user);
    }

    @DeleteMapping("/{id}")
    public UserBasicDTO deleteUserById(@PathVariable long id) {
        User user = userService.deleteUserById(id);
        return mapper.toDTO(user);
    }
}