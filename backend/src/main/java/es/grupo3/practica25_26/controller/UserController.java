package es.grupo3.practica25_26.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import es.grupo3.practica25_26.model.Error;
import es.grupo3.practica25_26.model.Image;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.service.ErrorService;
import es.grupo3.practica25_26.service.ImageService;
import es.grupo3.practica25_26.service.SampleDataService;
import es.grupo3.practica25_26.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ErrorService errorService;

    @Autowired
    ImageService imageService;

    @Autowired
    SampleDataService dataService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/user_register")
    public String userRegister(Model model, User newUser, HttpSession session, HttpServletRequest request,
            MultipartFile imageFile)
            throws IOException, ServletException {

        Error error = null;
        String errorTitle = "";
        String errorMessage = "";

        Optional<User> op = userService.findUserByEmail(newUser.getEmail());
        if (op.isPresent()) {
            session.setAttribute("user_failed_register", newUser);

            errorTitle = "El e-mail escogido está en uso.";
            errorMessage = "El correo electrónico introducido en el fomulario de registro ya pertenece a otro usuario. Por favor, utiliza otro correo electrónico para registrarte.";

            error = new Error(errorTitle, errorMessage);
        } else if (newUser.getUserName().length() > 8) {
            session.setAttribute("user_failed_register", newUser);

            errorTitle = "¡Demasiados caracteres en tu nombre!";
            errorMessage = "Tu nombre de usuario no debe tener más de 8 caracteres. Has introducido "
                    + newUser.getUserName().length() + " caracteres.";

            error = new Error(errorTitle, errorMessage);

        } else if (newUser.getSurname().length() > 30) {
            session.setAttribute("user_failed_register", newUser);

            errorTitle = "¡Demasiados caracteres en tu apellido!";
            errorMessage = "Tu apellido no debe tener más de 30 caracteres. Has introducido "
                    + newUser.getSurname().length() + " caracteres.";

            error = new Error(errorTitle, errorMessage);

        } else if (newUser.getAddress().length() > 30) {
            session.setAttribute("user_failed_register", newUser);

            errorTitle = "¡Demasiados caracteres en tu dirección!";
            errorMessage = "Tu dirección no debe tener más de 30 caracteres. Has introducido "
                    + newUser.getAddress().length() + " caracteres.";

            error = new Error(errorTitle, errorMessage);

        } else if (newUser.getUserName().length() == 0 || newUser.getSurname().length() == 0 ||
                newUser.getAddress().length() == 0 || newUser.getEmail().length() == 0
                || newUser.getEncodedPassword().length() == 0) {
            session.setAttribute("user_failed_register", newUser);

            errorTitle = "¡Formulario incompleto!";
            errorMessage = "No has rellenado todos los campos obligatorios del formulario.";

            error = new Error(errorTitle, errorMessage);

        } else if (newUser.getEmail().indexOf("@") == -1) {
            session.setAttribute("user_failed_register", newUser);

            errorTitle = "¡E-mail inválido!";
            errorMessage = "El correo electrónico que has introducido no es correcto.";

            error = new Error(errorTitle, errorMessage);

        } else if (newUser.getEncodedPassword().length() < 8 || newUser.getEncodedPassword().length() > 20) {
            session.setAttribute("user_failed_register", newUser);

            errorTitle = "¡Contraseña inválida!";
            errorMessage = "Tu contraseña debe tener entre 8 y 20 caracteres. Has introducido "
                    + newUser.getEncodedPassword().length() + " caracteres.";

            error = new Error(errorTitle, errorMessage);
        }

        if (error != null) {
            return errorService.setErrorPageWithButton(model, session, errorTitle, errorMessage, "Volver al registro",
                    "/signup");
        } else {
            String pass = newUser.getEncodedPassword();
            userService.addRoles(newUser, "USER");
            newUser.setPassword(passwordEncoder.encode(pass));
            userService.saveUser(newUser);

            if (!imageFile.isEmpty()) {
                Image image = imageService.createImage(imageFile.getInputStream());
                userService.addImageToUser(newUser.getId(), image);
            }

            userService.saveUser(newUser);

            try {
                request.login(newUser.getEmail(), pass);
            } catch (ServletException e) {
                e.printStackTrace();
            }

            return "redirect:/";
        }
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        return "profile";
    }

    @GetMapping("/profile/edit")
    public String editProfile(Model model, HttpSession session) {
        return "profile_edit";
    }

    @PostMapping("/profile/update")
    public String updateProfile(Model model, HttpSession session, @RequestParam String userName,
            @RequestParam String surname,
            @RequestParam String email, @RequestParam String address, @RequestParam MultipartFile imageFile)
            throws IOException {

        Error error = null;
        String errorTitle = "";
        String errorMessage = "";

        User currentUser = (User) session.getAttribute("currentUser");

        User updatedUser = new User(userName, surname, email, address, currentUser.getEncodedPassword());
        Optional<User> op = userService.findUserByEmail(email);
        if (!op.isPresent()) { // cambiar por el request
            session.setAttribute("user_failed_update", updatedUser);

            errorTitle = "El e-mail escogido está en uso.";
            errorMessage = "El correo electrónico introducido en el fomulario de registro ya pertenece a otro usuario. Por favor, utiliza otro correo electrónico.";

            error = new Error(errorTitle, errorMessage);
        } else if (userName.length() > 8) {
            session.setAttribute("user_failed_update", updatedUser);

            errorTitle = "¡Demasiados caracteres en tu nombre!";
            errorMessage = "Tu nombre de usuario no debe tener más de 8 caracteres. Has introducido "
                    + updatedUser.getUserName().length() + " caracteres.";

            error = new Error(errorTitle, errorMessage);
        } else if (surname.length() > 30) {
            session.setAttribute("user_failed_update", updatedUser);

            errorTitle = "¡Demasiados caracteres en tu apellido!";
            errorMessage = "Tu apellido no debe tener más de 30 caracteres. Has introducido "
                    + updatedUser.getSurname().length() + " caracteres.";

            error = new Error(errorTitle, errorMessage);
        } else if (email.indexOf("@") == -1) {
            session.setAttribute("user_failed_update", updatedUser);

            errorTitle = "¡E-mail inválido!";
            errorMessage = "El correo electrónico que has introducido no es correcto.";

            error = new Error(errorTitle, errorMessage);
        } else if (email.length() > 50) {
            session.setAttribute("user_failed_update", updatedUser);

            errorTitle = "¡E-mail inválido!";
            errorMessage = "El correo electrónico que has introducido no es correcto.";

            error = new Error(errorTitle, errorMessage);
        } else if (address.length() > 30) {
            session.setAttribute("user_failed_update", updatedUser);

            errorTitle = "¡Demasiados caracteres en tu dirección!";
            errorMessage = "Tu dirección no debe tener más de 30 caracteres. Has introducido "
                    + updatedUser.getAddress().length() + " caracteres.";

            error = new Error(errorTitle, errorMessage);
        } else if (userName.length() == 0 || surname.length() == 0 || email.length() == 0 || address.length() == 0) {
            session.setAttribute("user_failed_update", updatedUser);

            errorTitle = "¡Formulario incompleto!";
            errorMessage = "No has rellenado todos los campos obligatorios del formulario.";

            error = new Error(errorTitle, errorMessage);
        }

        if (error != null) {

            return errorService.setErrorPageWithButton(model, session, error.getTitle(), error.getMessage(),
                    "Volver a edición de perfil", "/profile/edit");
        } else {
            return "redirect:/profile";
        }
    }

    @PostMapping("profile/update_password")
    public String updatePassword(Model model, HttpSession session, @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        Error error = null;
        String errorTitle = "";
        String errorMessage = "";

        User currentUser = (User) session.getAttribute("currentUser");
        Optional<User> op = userService.findUserByEmail(currentUser.getEmail());

        if (!op.isPresent()) {

            errorTitle = "¡Contraseña incorrecta!";
            errorMessage = "No has introducido correctamente tu contraseña actual, por lo que no puedes establecer la nueva.";

            error = new Error(errorTitle, errorMessage);
        } else if (newPassword.length() == 0 || oldPassword.length() == 0) {

            errorTitle = "¡Formulario incompleto!";
            errorMessage = "No has rellenado todos los campos obligatorios del formulario.";

            error = new Error(errorTitle, errorMessage);
        } else if (newPassword.length() < 8 || newPassword.length() > 20) {

            errorTitle = "¡Contraseña inválida!";
            errorMessage = "Tu contraseña debe tener entre 8 y 20 caracteres. Has introducido "
                    + currentUser.getEncodedPassword().length() + " caracteres.";

            error = new Error(errorTitle, errorMessage);
        }
        if (error != null) {
            session.setAttribute("user_failed_update", currentUser);
            session.setAttribute("failed_password", newPassword);
            return errorService.setErrorPageWithButton(model, session, error.getTitle(), error.getMessage(),
                    "Volver a edición de perfil", "/profile/edit");
        }

        userService.updateUserPassword(currentUser, newPassword);
        session.setAttribute("user", currentUser);

        return "redirect:/profile";
    }
}
