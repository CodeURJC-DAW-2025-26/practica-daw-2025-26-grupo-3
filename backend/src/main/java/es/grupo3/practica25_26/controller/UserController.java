package es.grupo3.practica25_26.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.service.ErrorService;
import es.grupo3.practica25_26.service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ErrorService errorService;

    @PostMapping("/user_register")
    public String userRegister(Model model, User newUser, HttpSession session) {
        Optional<User> op = userService.findUserByEmail(newUser.getEmail());
        if (op.isPresent()) {
            session.setAttribute("user_failed_register", newUser);
            session.setAttribute("user_logged", false);
            return errorService.setErrorPageWithButton(model, session, "El e-mail escogido está en uso.",
                    "El correo electrónico introducido en el fomulario de registro ya pertenece a otro usuario. Por favor, utiliza otro correo electrónico para registrarte.",
                    "Volver al registro", "/signup");
        } else if (newUser.getUserName().length() > 8) {
            session.setAttribute("user_failed_register", newUser);
            session.setAttribute("user_logged", false);
            return errorService.setErrorPageWithButton(model, session, "¡Demasiados caracteres en tu nombre!",
                    "Tu nombre de usuario no debe tener más de 8 caracteres. Has introducido "
                            + newUser.getUserName().length() + " caracteres.",
                    "Volver al registro", "/signup");
        } else if (newUser.getSurname().length() > 30) {
            session.setAttribute("user_failed_register", newUser);
            session.setAttribute("user_logged", false);
            return errorService.setErrorPageWithButton(model, session, "¡Demasiados caracteres en tu apellido!",
                    "Tu apellido no debe tener más de 30 caracteres. Has introducido "
                            + newUser.getSurname().length() + " caracteres.",
                    "Volver al registro", "/signup");
        } else if (newUser.getAddress().length() > 30) {
            session.setAttribute("user_failed_register", newUser);
            session.setAttribute("user_logged", false);
            return errorService.setErrorPageWithButton(model, session, "¡Demasiados caracteres en tu dirección!",
                    "Tu dirección no debe tener más de 30 caracteres. Has introducido "
                            + newUser.getAddress().length() + " caracteres.",
                    "Volver al registro", "/signup");
        } else if (newUser.getUserName().length() == 0 || newUser.getSurname().length() == 0 ||
                newUser.getAddress().length() == 0 || newUser.getEmail().length() == 0
                || newUser.getPassword().length() == 0) {
            session.setAttribute("user_failed_register", newUser);
            session.setAttribute("user_logged", false);
            return errorService.setErrorPageWithButton(model, session, "¡Formulario incompleto!",
                    "No has rellenado todos los campos obligatorios del formulario.",
                    "Volver al registro", "/signup");
        } else if (newUser.getEmail().indexOf("@") == -1) {
            session.setAttribute("user_failed_register", newUser);
            session.setAttribute("user_logged", false);
            return errorService.setErrorPageWithButton(model, session, "¡E-mail inválido!",
                    "El correo electrónico introducido no es válido",
                    "Volver al registro", "/signup");
        } else if (newUser.getPassword().length() < 8 || newUser.getPassword().length() > 20) {
            session.setAttribute("user_failed_register", newUser);
            session.setAttribute("user_logged", false);
            return errorService.setErrorPageWithButton(model, session, "¡Contraseña inválida!",
                    "Tu contraseña debe tener entre 8 y 20 caracteres. Has introducido "
                            + newUser.getPassword().length() + " caracteres.",
                    "Volver al registro", "/signup");
        } else {
            session.setAttribute("currentUser", newUser);
            userService.saveUser(newUser);
            session.setAttribute("user_logged", true);
            return "redirect:/";
        }
    }

    @PostMapping("/login_query")
    public String loginQuery(Model model, @RequestParam String email, @RequestParam String password,
            HttpSession session) {
        Optional<User> op = userService.findUserByLogin(email, password);
        if (op.isPresent()) { // If credentials are correct and user is found
            User user = op.get();
            session.setAttribute("user_logged", true);
            session.setAttribute("currentUser", user);
        } else {
            session.setAttribute("user_logged", false);
        }
        return "redirect:/";
    }

    @GetMapping("/log_out")
    public String logOut(HttpSession session) {
        userService.logOut(session);
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        User currentUser = userService.getCurrentUser(session);
        model.addAttribute("user", currentUser);
        return "profile";
    }

}
