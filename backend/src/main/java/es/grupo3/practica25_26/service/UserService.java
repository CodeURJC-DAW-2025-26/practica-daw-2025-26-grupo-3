package es.grupo3.practica25_26.service;

import es.grupo3.practica25_26.model.Image;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.model.Error;
import es.grupo3.practica25_26.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//import jakarta.servlet.http.HttpSession;

@Service
public class UserService {

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // DATABASE QUERIES

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void saveAll(List<User> users) {
        userRepository.saveAll(users);
    }

    public long count() {
        return userRepository.count();
    }

    public User findUserByEmail(String email) {
        Optional<User> op = userRepository.findDistinctByEmail(email);
        if (op.isPresent()) {
            User user = op.get();
            return user;
        } else {
            return null;
        }
    }

    // FORMS FIELD VALIDATION METHODS

    public Error userNameCheck(String userName) {
        if (userName.length() > 15) {
            return new Error("¡Demasiados caracteres en tu nombre!",
                    "Tu nombre de usuario no debe tener más de 15 caracteres. Has introducido " +
                            +userName.length() + " caracteres.");
        }
        return null;
    }

    public Error surnameCheck(String surname) {
        if (surname.length() > 30) {
            return new Error("¡Demasiados caracteres en tu apellido!",
                    "Tu apellido no debe tener más de 30 caracteres. Has introducido " +
                            +surname.length() + " caracteres.");
        }
        return null;
    }

    public Error addressCheck(String address) {
        if (address.length() > 30) {
            return new Error("¡Demasiados caracteres en tu dirección!",
                    "Tu dirección no debe tener más de 15 caracteres. Has introducido " +
                            +address.length() + " caracteres.");
        }
        return null;
    }

    public Error emailCheck(String email) {
        if (email.indexOf("@") == -1) {
            return new Error("¡E-mail inválido!", "El correo electrónico que has introducido no es correcto.");
        }
        return null;
    }

    public Error passwordCheck(String password) {
        if (password.length() < 8 || password.length() > 30) {
            return new Error("¡Contraseña inválida!",
                    "La contraseña debe contene entre 8 y 30 caracteres. Has introducido: " + password.length()
                            + " caracteres.");
        }
        return null;
    }

    public Error correctPassCheck(String password, HttpServletRequest request) {
        String email = request.getUserPrincipal().getName();
        User currentUser = this.findUserByEmail(email);

        if (!passwordEncoder.matches(currentUser.getEncodedPassword(), passwordEncoder.encode(password))) {
            return new Error("¡Contraseña incorrecta!", "La contraseña introducida no es correcta.");
        }
        return null;
    }

    public Error uniqueEmailCheck(String email, HttpServletRequest request) {
        String currentEmail = request.getUserPrincipal().getName();
        if (!email.equals(currentEmail) && this.findUserByEmail(email) != null) {
            return new Error("El e-mail escogido está en uso.",
                    "El correo electrónico introducido en el fomulario de registro ya pertenece a otro usuario. Por favor, utiliza otro correo electrónico.");
        }
        return null;
    }

    public Error notFilledFormCheck(String... fields) {
        boolean error = false;
        for (String field : fields) {
            if (field.length() == 0) {
                error = true;
            }
        }

        if (error) {
            return new Error("¡Formulario incompleto!",
                    "No has rellenado todos los campos obligatorios del formulario.");
        }
        return null;
    }

    // OTHER METHODS

    public void updateUserInfo(User currentUser, String userName, String surname, String email, String address) {

        currentUser.setUserName(userName);
        currentUser.setSurname(surname);
        currentUser.setEmail(email);
        currentUser.setAddress(address);
    }

    public void updateUserPassword(User currentUser, String newPassword) {
        currentUser.setPassword(newPassword);
    }

    public User addImageToUser(long id, Image image) {
        User user = userRepository.findById(id).orElseThrow();

        if (user.getImage() != null) { // If user has image, we change the image file
            user.getImage().setImageFile(image.getImageFile());
        } else { // Else, we set the new image to the user, and the user to the image.
            user.setImage(image);
            image.setUser(user);
        }

        userRepository.save(user);
        return user;
    }

    public void addRoles(User user, String... roles) {
        List<String> roleList = user.getRoles();
        if (roleList == null) {
            roleList = new ArrayList<>();
            user.setRoles(roleList);
        }

        for (String role : roles) {
            roleList.add(role);
        }
    }
}