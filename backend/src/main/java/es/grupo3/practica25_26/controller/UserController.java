package es.grupo3.practica25_26.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.model.Error;
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
        Error error = null;
        String errorTitle = "";
        String errorMessage = "";
        if (op.isPresent()) {
            session.setAttribute("user_failed_register", newUser);
            session.setAttribute("user_logged", false);

            errorTitle = "El e-mail escogido está en uso.";
            errorMessage = "El correo electrónico introducido en el fomulario de registro ya pertenece a otro usuario. Por favor, utiliza otro correo electrónico para registrarte.";

            error = new Error(errorTitle, errorMessage);
        } else if (newUser.getUserName().length() > 8) {
            session.setAttribute("user_failed_register", newUser);
            session.setAttribute("user_logged", false);

            errorTitle = "¡Demasiados caracteres en tu nombre!";
            errorMessage = "Tu nombre de usuario no debe tener más de 8 caracteres. Has introducido "
                    + newUser.getUserName().length() + " caracteres.";

            error = new Error(errorTitle, errorMessage);

        } else if (newUser.getSurname().length() > 30) {
            session.setAttribute("user_failed_register", newUser);
            session.setAttribute("user_logged", false);

            errorTitle = "¡Demasiados caracteres en tu apellido!";
            errorMessage = "Tu apellido no debe tener más de 30 caracteres. Has introducido "
                    + newUser.getSurname().length() + " caracteres.";

            error = new Error(errorTitle, errorMessage);

        } else if (newUser.getAddress().length() > 30) {
            session.setAttribute("user_failed_register", newUser);
            session.setAttribute("user_logged", false);

            errorTitle = "¡Demasiados caracteres en tu dirección!";
            errorMessage = "Tu dirección no debe tener más de 30 caracteres. Has introducido "
                    + newUser.getAddress().length() + " caracteres.";

            error = new Error(errorTitle, errorMessage);

        } else if (newUser.getUserName().length() == 0 || newUser.getSurname().length() == 0 ||
                newUser.getAddress().length() == 0 || newUser.getEmail().length() == 0
                || newUser.getPassword().length() == 0) {
            session.setAttribute("user_failed_register", newUser);
            session.setAttribute("user_logged", false);

            errorTitle = "¡Formulario incompleto!";
            errorMessage = "No has rellenado todos los campos obligatorios del formulario.";

            error = new Error(errorTitle, errorMessage);

        } else if (newUser.getEmail().indexOf("@") == -1) {
            session.setAttribute("user_failed_register", newUser);
            session.setAttribute("user_logged", false);

            errorTitle = "¡E-mail inválido!";
            errorMessage = "El correo electrónico que has introducido no es correcto.";

            error = new Error(errorTitle, errorMessage);

        } else if (newUser.getPassword().length() < 8 || newUser.getPassword().length() > 20) {
            session.setAttribute("user_failed_register", newUser);
            session.setAttribute("user_logged", false);

            errorTitle = "¡Contraseña inválida!";
            errorMessage = "Tu contraseña debe tener entre 8 y 20 caracteres. Has introducido "
                    + newUser.getPassword().length() + " caracteres.";

            error = new Error(errorTitle, errorMessage);
        }

        if (error != null) {
            return errorService.setErrorPageWithButton(model, session, errorTitle, errorMessage, "Volver al registro",
                    "/signup");
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

    @GetMapping("/profile/edit")
    public String editProfile(Model model, HttpSession session) {
        User failedUser = (User) session.getAttribute("user_failed_update");
        if (failedUser != null) {
            model.addAttribute("user", failedUser);
            session.removeAttribute("user_failed_update");
        } else {
            User currentUser = (User) session.getAttribute("currentUser");
            model.addAttribute("user", currentUser);
        }
        return "profile_edit";
    }

    @PostMapping("/profile/update")
    public String updateProfile(Model model, HttpSession session, @RequestParam String userName,
            @RequestParam String surname,
            @RequestParam String email, @RequestParam String address) {

        Error error = null;
        String errorTitle = "";
        String errorMessage = "";
        User currentUser = (User) session.getAttribute("currentUser");
        User updatedUser = new User(userName, surname, email, address, currentUser.getPassword());
        Optional<User> op = userService.findUserByEmail(email);
        if (!op.isPresent()) {
            session.setAttribute("user_failed_update", updatedUser);
            session.setAttribute("user_logged", true);

            errorTitle = "El e-mail escogido está en uso.";
            errorMessage = "El correo electrónico introducido en el fomulario de registro ya pertenece a otro usuario. Por favor, utiliza otro correo electrónico.";

            error = new Error(errorTitle, errorMessage);
        } else if (userName.length() > 8) {
            session.setAttribute("user_failed_update", updatedUser);
            session.setAttribute("user_logged", true);

            errorTitle = "¡Demasiados caracteres en tu nombre!";
            errorMessage = "Tu nombre de usuario no debe tener más de 8 caracteres. Has introducido "
                    + updatedUser.getUserName().length() + " caracteres.";

            error = new Error(errorTitle, errorMessage);
        } else if (surname.length() > 30) {
            session.setAttribute("user_failed_update", updatedUser);
            session.setAttribute("user_logged", true);

            errorTitle = "¡Demasiados caracteres en tu apellido!";
            errorMessage = "Tu apellido no debe tener más de 30 caracteres. Has introducido "
                    + updatedUser.getSurname().length() + " caracteres.";

            error = new Error(errorTitle, errorMessage);
        } else if (email.indexOf("@") == -1) {
            session.setAttribute("user_failed_update", updatedUser);
            session.setAttribute("user_logged", true);

            errorTitle = "¡E-mail inválido!";
            errorMessage = "El correo electrónico que has introducido no es correcto.";

            error = new Error(errorTitle, errorMessage);
        } else if (email.length() > 50) {
            session.setAttribute("user_failed_update", updatedUser);
            session.setAttribute("user_logged", true);

            errorTitle = "¡E-mail inválido!";
            errorMessage = "El correo electrónico que has introducido no es correcto.";

            error = new Error(errorTitle, errorMessage);
        } else if (address.length() > 30) {
            session.setAttribute("user_failed_update", updatedUser);
            session.setAttribute("user_logged", true);

            errorTitle = "¡Demasiados caracteres en tu dirección!";
            errorMessage = "Tu dirección no debe tener más de 30 caracteres. Has introducido "
                    + updatedUser.getAddress().length() + " caracteres.";

            error = new Error(errorTitle, errorMessage);
        } else if (userName.length() == 0 || surname.length() == 0 || email.length() == 0 || address.length() == 0) {
            session.setAttribute("user_failed_update", updatedUser);
            session.setAttribute("user_logged", true);

            errorTitle = "¡Formulario incompleto!";
            errorMessage = "No has rellenado todos los campos obligatorios del formulario.";

            error = new Error(errorTitle, errorMessage);
        }

        if (error != null) {
            return errorService.setErrorPageWithButton(model, session, errorTitle, errorMessage,
                    "Volver a edición de perfil", "/profile/edit");
        } else {
            userService.updateUserInfo(currentUser, userName, surname, email, address);
            session.setAttribute("currentUser", updatedUser);
            model.addAttribute("user", updatedUser);
            return "profile";
        }
    }
}
