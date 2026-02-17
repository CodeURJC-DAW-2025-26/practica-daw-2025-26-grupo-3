package es.grupo3.practica25_26.service;

import es.grupo3.practica25_26.model.Image;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.repository.UserRepository;

import java.util.List;
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

    public void saveAll(List<User> users) {
        userRepository.saveAll(users);
    }

    public long count() {
        return userRepository.count();
    }

    public Optional<User> findUserByLogin(String email, String pass) {
        return userRepository.findByEmailAndPassword(email, pass);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findDistinctByEmail(email);
    }

    // GET CURRENT USER INFO

    public void getUserNavInfo(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        boolean user_logged = (currentUser != null);

        if (user_logged) {
            model.addAttribute("userName", currentUser.getUserName());
            if (currentUser.getImage() != null) {
                model.addAttribute("has_image", true);
                model.addAttribute("id", currentUser.getImage().getId());
            } else {
                model.addAttribute("has_image", false);
            }
        }

        model.addAttribute("user_logged", user_logged);
    }

    public User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("currentUser");
    }

    // OTHER METHODS

    public void logOut(HttpSession session) {
        session.invalidate();
    }

    public void updateUserInfo(User currentUser, String userName, String surname, String email, String address) {

        currentUser.setUserName(userName);
        currentUser.setSurname(surname);
        currentUser.setEmail(email);
        currentUser.setAddress(address);

        userRepository.save(currentUser);
    }

    public void updateUserPassword(User currentUser, String newPassword) {
        currentUser.setPassword(newPassword);
        userRepository.save(currentUser);
    }

    public User addImageToUser(long id, Image image) {
        User user = userRepository.findById(id).orElseThrow();
        user.setImage(image);
        image.setUser(user);
        userRepository.save(user);
        return user;
    }

}