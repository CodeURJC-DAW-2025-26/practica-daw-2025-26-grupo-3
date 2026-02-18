package es.grupo3.practica25_26.service;

import es.grupo3.practica25_26.model.Image;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

        if (user.getImage() != null) { // If user has image, we change the image file
            user.getImage().setImageFile(image.getImageFile());
        } else { // Else, we set the new image to the user, and the user to the image.
            user.setImage(image);
            image.setUser(user);
        }

        userRepository.save(user);
        return user;
    }

    /*
     * public User addImageToUser(long id, Blob imageBlob) {
     * User user = userRepository.findById(id).orElseThrow();
     * 
     * if (user.getImage() != null) {
     * user.getImage().setImageFile(imageBlob);
     * } else {
     * Image image = new Image(imageBlob);
     * user.setImage(image);
     * image.setUser(user);
     * }
     * return user;
     * }
     */

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