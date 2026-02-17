package es.grupo3.practica25_26.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import es.grupo3.practica25_26.model.Error;
import es.grupo3.practica25_26.model.Image;
import es.grupo3.practica25_26.model.Role;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.service.ErrorService;
import es.grupo3.practica25_26.service.ImageService;
import es.grupo3.practica25_26.service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ErrorService errorService;

    @Autowired
    ImageService imageService;

    @PostMapping("/user_register")
    public String userRegister(Model model, User newUser, HttpSession session,
            @RequestParam("imageFile") MultipartFile imageFile)
            throws Exception {
        // force default role for new registrations.
        newUser.setRole(Role.USER);
        Optional<User> op = userService.findUserByEmail(newUser.getEmail());
        Error error = null;
        String errorTitle = "";
        String errorMessage = "";
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
                || newUser.getPassword().length() == 0) {
            session.setAttribute("user_failed_register", newUser);

            errorTitle = "¡Formulario incompleto!";
            errorMessage = "No has rellenado todos los campos obligatorios del formulario.";

            error = new Error(errorTitle, errorMessage);

        } else if (newUser.getEmail().indexOf("@") == -1) {
            session.setAttribute("user_failed_register", newUser);

            errorTitle = "¡E-mail inválido!";
            errorMessage = "El correo electrónico que has introducido no es correcto.";

            error = new Error(errorTitle, errorMessage);

        } else if (newUser.getPassword().length() < 8 || newUser.getPassword().length() > 20) {
            session.setAttribute("user_failed_register", newUser);

            errorTitle = "¡Contraseña inválida!";
            errorMessage = "Tu contraseña debe tener entre 8 y 20 caracteres. Has introducido "
                    + newUser.getPassword().length() + " caracteres.";

            error = new Error(errorTitle, errorMessage);
        }

        if (error != null) {
            return errorService.setErrorPageWithButton(model, session, errorTitle, errorMessage, "Volver al registro",
                    "/signup");
        } else {
            newUser.setRole(Role.USER);
            session.setAttribute("currentUser", newUser);
            userService.saveUser(newUser);

            if (!imageFile.isEmpty()) {
                Image image = imageService.createImage(imageFile.getInputStream());
                userService.addImageToUser(newUser.getId(), image);
            }

            return "redirect:/";
        }
    }

    @PostMapping("/login_query")
    public String loginQuery(Model model, @RequestParam String email, @RequestParam String password,
            HttpSession session) {
        Optional<User> op = userService.findUserByLogin(email, password);
        // If credentials are correct and user is found
        if (op.isPresent()) {
            User user = op.get();
            session.setAttribute("currentUser", user);
            // Redirect to admin panel if user is admin
            if (user.getRole().equals(Role.ADMIN)) {
                return "redirect:/admin_panel";
            }
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

        Image image = currentUser.getImage();

        if (image != null) {
            model.addAttribute("has_image", true);
            model.addAttribute("id", image.getId());
        } else {
            model.addAttribute("has_image", false);
        }

        model.addAttribute("user", currentUser);
        return "profile";
    }

    @GetMapping("/profile/edit")
    public String editProfile(Model model, HttpSession session) {
        User failedUser = (User) session.getAttribute("user_failed_update");
        String failedPassword = (String) session.getAttribute("failed_password");
        if (failedUser != null && failedPassword == null) {
            model.addAttribute("user", failedUser);
            model.addAttribute("newPassword", "");

            User currentUser = userService.getCurrentUser(session);
            if (currentUser.getImage() != null) {
                model.addAttribute("has_image", true);
                model.addAttribute("id", currentUser.getImage().getId());
            } else {
                model.addAttribute("has_image", false);
            }

            session.removeAttribute("user_failed_update");
        } else if (failedPassword != null) {
            User currentUser = userService.getCurrentUser(session);
            model.addAttribute("user", currentUser);

            if (currentUser.getImage() != null) {
                model.addAttribute("has_image", true);
                model.addAttribute("id", currentUser.getImage().getId());
            } else {
                model.addAttribute("has_image", false);
            }

            model.addAttribute("newPassword", failedPassword);
            session.removeAttribute("failed_password");
            if (failedUser != null) {
                session.removeAttribute("user_failed_update");
                session.removeAttribute("user_failed_update");
            }
        } else {
            User currentUser = userService.getCurrentUser(session);

            Image image = currentUser.getImage();

            if (image != null) {
                model.addAttribute("has_image", true);
                model.addAttribute("id", image.getId());
            } else {
                model.addAttribute("has_image", false);
            }
            model.addAttribute("user", currentUser);
            model.addAttribute("newPassword", "");
        }
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
        User currentUser = userService.getCurrentUser(session);
        User updatedUser = new User(userName, surname, address, email, currentUser.getPassword(),
                currentUser.getRole());

        Image updatedImage = null;
        if (!imageFile.isEmpty()) {
            updatedImage = imageService.createImage(imageFile.getInputStream());
        } else {
            updatedImage = currentUser.getImage();
        }

        Optional<User> op = userService.findUserByEmail(email);
        if (!op.isPresent()) {
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
            userService.getUserNavInfo(model, session);
            return errorService.setErrorPageWithButton(model, session, error.getTitle(), error.getMessage(),
                    "Volver a edición de perfil", "/profile/edit");
        } else {
            userService.updateUserInfo(currentUser, userName, surname, email, address);
            User updatedUserWithImage = userService.addImageToUser(currentUser.getId(), updatedImage);
            session.setAttribute("currentUser", updatedUserWithImage);
            model.addAttribute("user", updatedUserWithImage);
            return "redirect:/profile";
        }
    }

    @PostMapping("profile/update_password")
    public String updatePassword(Model model, HttpSession session, @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        Error error = null;
        String errorTitle = "";
        String errorMessage = "";
        User currentUser = userService.getCurrentUser(session);
        Optional<User> op = userService.findUserByLogin(currentUser.getEmail(), oldPassword);

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
                    + currentUser.getPassword().length() + " caracteres.";

            error = new Error(errorTitle, errorMessage);
        }
        if (error != null) {
            userService.getUserNavInfo(model, session);
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
