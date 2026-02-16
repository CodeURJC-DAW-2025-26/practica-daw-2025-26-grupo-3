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

    // DATABASE QUERIES

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public Optional<User> findUserByLogin(String email, String pass) {
        return userRepository.findByEmailAndPassword(email, pass);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findDistinctByEmail(email);
    }

    // OTTHER METHODS

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

    public void logOut(HttpSession session) {
        session.invalidate();
    }

    public User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("currentUser");
    }

    public void updateUserInfo(User currentUser, String userName, String surname, String email, String address) {

        currentUser.setUserName(userName);
        currentUser.setSurname(surname);
        currentUser.setEmail(email);
        currentUser.setAddress(address);

        userRepository.save(currentUser);
    }
}