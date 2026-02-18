package es.grupo3.practica25_26.service;

import es.grupo3.practica25_26.model.Image;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import jakarta.servlet.http.HttpSession;

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

    /*  Spring already checks the password automatically, so this is not necessary
    public User findUserByLogin(String email, String pass) {
        Optional<User> op = userRepository.findByEmailAndPassword(email, pass);
        if (op.isPresent()) {
            User user = op.get();
            return user;
        } else {
            return null;
        }
    }
        */

    public User findUserByEmail(String email) {
        Optional<User> op = userRepository.findDistinctByEmail(email);
        if (op.isPresent()) {
            User user = op.get();
            return user;
        } else {
            return null;
        }
    }

    // OTHER METHODS

    /* Spring handles logout in the configuration when its necessary
    public void logOut(HttpSession session) {
        session.invalidate();
    }
    */

    public void updateUserInfo(User currentUser, String userName, String surname, String email, String address) {

        currentUser.setUserName(userName);
        currentUser.setSurname(surname);
        currentUser.setEmail(email);
        currentUser.setAddress(address);
    }

    public void updateUserPassword(User currentUser, String newPassword) {
        currentUser.setPassword(newPassword);
        userRepository.save(currentUser);
    }

    public User addImageToUser(long id, Image image) {
        User user = userRepository.findById(id).orElseThrow();

        if (user.getImage() != null) { // If user has image, we change the image file
            user.getImage().setImageFile(image.getImageFile());
        } else { // Else, we set the new image to the user, and the user to the image.
            user.setImage(image);
            image.setUser(user);
        }

        userRepository.save(user);
        return user;
    }

    public void addRoles(User user, String... roles) {
        List<String> roleList = user.getRoles();
        if (roleList == null) {
            roleList = new ArrayList<>();
            user.setRoles(roleList);
        }

        for (String role : roles) {
            roleList.add(role);
        }
    }

}