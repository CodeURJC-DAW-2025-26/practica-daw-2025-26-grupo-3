package es.grupo3.practica25_26.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.repository.UserRepository;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/user_register")
    public String userRegister(Model model, User newUser, HttpSession sesion) {
        userRepository.save(newUser);
        sesion.setAttribute("currentUser", newUser);
        sesion.setAttribute("user_logged", true);
        return "redirect:/";
    }

    @PostMapping("/login_query")
    public String loginQuery(Model model, @RequestParam String email, @RequestParam String password,
            HttpSession sesion) {
        Optional<User> op = userRepository.findByEmailAndPassword(email, password);
        if (op.isPresent()) { // If credentials are correct and user is found
            User user = op.get();
            sesion.setAttribute("user_logged", true);
            sesion.setAttribute("currentUser", user);
        } else {
            sesion.setAttribute("user_logged", false);
        }
        return "redirect:/";
    }

    @GetMapping("/log_out")
    public String logOut(HttpSession sesion) {
        sesion.invalidate();
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpSession sesion) {
        User currentUser = (User) sesion.getAttribute("currentUser");
        model.addAttribute("user", currentUser);
        return "profile";
    }

}
