package es.grupo3.practica25_26.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/user_register")
    public String userRegister(Model model, User newUser, HttpSession session) {
        userService.saveUser(newUser);
        session.setAttribute("currentUser", newUser);
        session.setAttribute("user_logged", true);
        return "redirect:/";
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
