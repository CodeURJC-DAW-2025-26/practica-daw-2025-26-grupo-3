package es.grupo3.practica25_26.controller;

import java.io.IOException;

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

        if (userService.findUserByEmail(newUser.getEmail()) != null) {
            error = new Error("El e-mail escogido está en uso.",
                    "El correo electrónico introducido en el fomulario de registro ya pertenece a otro usuario. Por favor, utiliza otro correo electrónico para registrarte.");
        }
        if (error == null) {
            error = userService.userNameCheck(newUser.getUserName());
        }
        if (error == null) {
            error = userService.surnameCheck(newUser.getSurname());
        }
        if (error == null) {
            error = userService.addressCheck(newUser.getAddress());
        }
        if (error == null) {
            error = userService.notFilledFormCheck(newUser.getUserName(), newUser.getSurname(), newUser.getAddress(),
                    newUser.getEmail(), newUser.getEncodedPassword());
        }
        if (error == null) {
            error = userService.emailCheck(newUser.getEmail());
        }
        if (error == null) {
            error = userService.passwordCheck(newUser.getEncodedPassword());
        }

        if (error != null) {
            session.setAttribute("user_failed_register", newUser);
            return errorService.setErrorPageWithButton(model, session, error.getTitle(), error.getMessage(),
                    "Volver al registro",
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
    public String profile() {
        return "profile";
    }

    @GetMapping("/profile/edit")
    public String editProfile(Model model, HttpSession session) {
        model.addAttribute("newPassword", "");
        return "profile_edit";
    }

    @PostMapping("/profile/update")
    public String updateProfile(Model model, HttpServletRequest request, HttpSession session,
            @RequestParam String userName,
            @RequestParam String surname,
            @RequestParam String email, @RequestParam String address, @RequestParam MultipartFile imageFile)
            throws IOException {

        Error error = null;

        String currentEmail = request.getUserPrincipal().getName();
        User currentUser = userService.findUserByEmail(currentEmail);

        if (!email.equals(currentEmail) && userService.findUserByEmail(email) != null) {
            error = new Error("El e-mail escogido está en uso.",
                    "El correo electrónico introducido en el fomulario de registro ya pertenece a otro usuario. Por favor, utiliza otro correo electrónico.");
        }

        if (error == null) {
            error = userService.userNameCheck(userName);
        }
        if (error == null) {
            error = userService.surnameCheck(surname);
        }
        if (error == null) {
            error = userService.emailCheck(email);
        }
        if (error == null) {
            error = userService.addressCheck(address);
        }
        if (error == null) {
            error = userService.notFilledFormCheck(userName, surname, email, address);
        }

        if (error != null) {
            User failedUser = new User(userName, surname, address, email, currentUser.getEncodedPassword());
            session.setAttribute("user_failed_update", failedUser);
            return errorService.setErrorPageWithButton(model, session, error.getTitle(), error.getMessage(),
                    "Volver a edición de perfil", "/profile/edit");
        } else {
            userService.updateUserInfo(currentUser, userName, surname, email, address);

            if (!imageFile.isEmpty()) {
                Image image = imageService.createImage(imageFile.getInputStream());
                userService.addImageToUser(currentUser.getId(), image);
            }

            userService.saveUser(currentUser);

            return "redirect:/profile";
        }
    }

    @PostMapping("profile/update_password")
    public String updatePassword(Model model, HttpSession session, HttpServletRequest request,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        Error error = null;

        String email = request.getUserPrincipal().getName();
        User currentUser = userService.findUserByEmail(email);

        if (error == null) {
            error = userService.notFilledFormCheck(newPassword, oldPassword);
        }
        if (error == null) {
            error = userService.passwordCheck(newPassword);
        }
        if (error == null) {
            error = userService.correctPassCheck(oldPassword, request);
        }

        if (error != null) {
            session.setAttribute("user_failed_update", currentUser);
            session.setAttribute("failed_password", newPassword);
            return errorService.setErrorPageWithButton(model, session, error.getTitle(), error.getMessage(),
                    "Volver a edición de perfil", "/profile/edit");
        } else {
            userService.updateUserPassword(currentUser, passwordEncoder.encode(newPassword));
            userService.saveUser(currentUser);
            return "redirect:/profile";
        }
    }
}
