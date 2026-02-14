package es.grupo3.practica25_26.service;

import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.repository.UserRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public void getUserNavInfo(Model model, HttpSession session) {
        User currentUser;
        boolean user_logged = true;
        try {
            user_logged = (boolean) session.getAttribute("user_logged");
            currentUser = (User) session.getAttribute("currentUser");
            model.addAttribute("userName", currentUser.getUserName());
        } catch (Exception e) { // If we get no data from session, then we asume that there is no logged user.
            user_logged = false;
        }
        model.addAttribute("user_logged", user_logged);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public Optional<User> findUserByLogin(String email, String pass) {
        return userRepository.findByEmailAndPassword(email, pass);
    }

    public void logOut(HttpSession session) {
        session.invalidate();
    }

    public User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("currentUser");
    }
}