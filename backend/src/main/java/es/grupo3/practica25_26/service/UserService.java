package es.grupo3.practica25_26.service;

import es.grupo3.practica25_26.model.Image;
import es.grupo3.practica25_26.model.Order;
import es.grupo3.practica25_26.model.OrderItem;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.model.Error;
import es.grupo3.practica25_26.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//import jakarta.servlet.http.HttpSession;

@Service
public class UserService {

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    public Error userRegisterCheck(User newUser) {
        if (findUserByEmail(newUser.getEmail()) != null) {
            return new Error("El e-mail escogido está en uso.",
                    "El correo electrónico introducido en el fomulario de registro ya pertenece a otro usuario. Por favor, utiliza otro correo electrónico para registrarte.");
        }
        Error error = userNameCheck(newUser.getUserName());
        if (error != null)
            return error;
        error = surnameCheck(newUser.getSurname());
        if (error != null)
            return error;
        error = addressCheck(newUser.getAddress());
        if (error != null)
            return error;
        error = notFilledFormCheck(newUser.getUserName(), newUser.getSurname(), newUser.getAddress(),
                newUser.getEmail(), newUser.getEncodedPassword());
        if (error != null)
            return error;
        error = emailCheck(newUser.getEmail());
        if (error != null)
            return error;
        error = passwordCheck(newUser.getEncodedPassword());
        return error;
    }

    public Error userUpdateCheck(String userName, String surname, String email, String address,
            HttpServletRequest request) {

        Error error = uniqueEmailCheck(email, request);
        if (error != null)
            return error;

        error = userNameCheck(userName);
        if (error != null)
            return error;
        error = surnameCheck(surname);
        if (error != null)
            return error;
        error = emailCheck(email);
        if (error != null)
            return error;
        error = addressCheck(address);
        if (error != null)
            return error;
        error = notFilledFormCheck(userName, surname, email, address);
        return error;
    }

    public Error uniqueEmailCheck(String email, HttpServletRequest request) {
        String currentEmail = request.getUserPrincipal().getName();
        if (!email.equals(currentEmail) && this.findUserByEmail(email) != null) {
            return new Error("El e-mail escogido está en uso.",
                    "El correo electrónico introducido en el fomulario de registro ya pertenece a otro usuario. Por favor, utiliza otro correo electrónico.");
        }
        return null;
    }

    public Error userPasswordUpdateCheck(String newPassword, String oldPassword, HttpServletRequest request) {
        Error error = notFilledFormCheck(newPassword, oldPassword);
        if (error != null)
            return error;
        error = passwordCheck(newPassword);
        if (error != null)
            return error;
        error = correctPassCheck(oldPassword, request);
        return error;
    }

    public Error userDeleteCheck(String currentPassword, boolean confirmDelete, HttpServletRequest request) {
        if (!confirmDelete) {
            return new Error("¡Pendiente de confirmación!",
                    "Por motivos de seguridad, debes marcar la casilla de confirmación para eliminar tu cuenta.");
        }
        Error error = correctPassCheck(currentPassword, request);
        return error;
    }

    public Error userBlockCheck(User userToBlock) {
        if (userToBlock == null) {
            return new Error("Usuario no encontrado", "No se ha encontrado el usuario a bloquear.");
        }
        if (!userToBlock.getState()) {
            return new Error("Usuario ya bloqueado", "El usuario ya está bloqueado.");
        }
        return null;
    }

    public Error userUnblockCheck(User userToUnblock) {
        if (userToUnblock == null) {
            return new Error("Usuario no encontrado", "No se ha encontrado el usuario a desbloquear.");
        }
        if (userToUnblock.getState()) {
            return new Error("Usuario ya desbloqueado", "El usuario ya está desbloqueado.");
        }
        return null;
    }

    UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

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

    public User findUserByEmail(String email) {
        Optional<User> op = userRepository.findDistinctByEmail(email);
        if (op.isPresent()) {
            User user = op.get();
            return user;
        } else {
            return null;
        }
    }

    // FORMS FIELD VALIDATION METHODS

    public Error userNameCheck(String userName) {
        if (userName.length() > 15) {
            return new Error("¡Demasiados caracteres en tu nombre!",
                    "Tu nombre de usuario no debe tener más de 15 caracteres. Has introducido " +
                            +userName.length() + " caracteres.");
        }
        return null;
    }

    public Error surnameCheck(String surname) {
        if (surname.length() > 30) {
            return new Error("¡Demasiados caracteres en tu apellido!",
                    "Tu apellido no debe tener más de 30 caracteres. Has introducido " +
                            +surname.length() + " caracteres.");
        }
        return null;
    }

    public Error addressCheck(String address) {
        if (address.length() > 30) {
            return new Error("¡Demasiados caracteres en tu dirección!",
                    "Tu dirección no debe tener más de 15 caracteres. Has introducido " +
                            +address.length() + " caracteres.");
        }
        return null;
    }

    public Error emailCheck(String email) {
        if (email.indexOf("@") == -1) {
            return new Error("¡E-mail inválido!", "El correo electrónico que has introducido no es correcto.");
        }
        return null;
    }

    public Error passwordCheck(String password) {
        if (password.length() < 8 || password.length() > 30) {
            return new Error("¡Contraseña inválida!",
                    "La contraseña debe contener entre 8 y 30 caracteres. Has introducido: " + password.length()
                            + " caracteres.");
        }
        return null;
    }

    public Error correctPassCheck(String password, HttpServletRequest request) {
        String email = request.getUserPrincipal().getName();
        User currentUser = this.findUserByEmail(email);

        if (!passwordEncoder.matches(password, currentUser.getEncodedPassword())) {
            return new Error("¡Contraseña incorrecta!", "La contraseña introducida no es correcta.");
        }
        return null;
    }

    public Error notFilledFormCheck(String... fields) {
        for (String field : fields) {
            if (field.length() == 0) {
                return new Error("¡Formulario incompleto!",
                        "No has rellenado todos los campos obligatorios del formulario.");
            }
        }
        return null;
    }

    // OTHER METHODS
    public void updateUserInfo(User currentUser, String userName, String surname, String email, String address) {
        currentUser.setUserName(userName);
        currentUser.setSurname(surname);
        currentUser.setEmail(email);
        currentUser.setAddress(address);
    }

    public void updateUserPassword(User currentUser, String newPassword) {
        currentUser.setPassword(newPassword);
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

    public void deleteUserById(Long id, HttpServletRequest request) throws NullPointerException {
        String email = request.getUserPrincipal().getName();
        Optional<User> op = userRepository.findDistinctByEmail(email);

        if (op.isPresent()) {
            User user = op.get();
            userRepository.deleteById(user.getId());
        } else {
            throw new NullPointerException("User was not found");
        }
    }

    // Obtain list of users without admins
    public List<User> getUsersWithoutAdmins() {
        return userRepository.findUsersWithoutRole("ADMIN");
    }

    // Change user's state to unblocked (true)
    public void unblockUser(User user) {
        user.setState(true);
        userRepository.save(user);
    }

    // Change user's state to blocked (false)
    public void blockUser(User user) {
        user.setState(false);
        userRepository.save(user);
    }

    // If we find the user by id, we return it, else we return null
    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public double getAllOrdersPrice(User user) {
        List<Order> orders = user.getOrders();
        double totalAmount = 0;
        for (Order order : orders) {
            order.calculateTotalPrice();
            totalAmount += order.getTotalPrice();
        }
        return totalAmount;
    }

    public void calculateFavouriteState(User user) {
        List<Order> orders = user.getOrders();
        List<OrderItem> orderItems;
        int newCounter = 0;
        int reconditionedCounter = 0;
        int secondHandCounter = 0;
        int state;

        // We count how many products of each type has de user buyed.
        for (Order order : orders) {
            orderItems = order.getOrderItems();
            for (OrderItem item : orderItems) {
                state = item.getProduct().getState();
                switch (state) {
                    case 0:
                        newCounter++;
                        break;
                    case 1:
                        reconditionedCounter++;
                        break;
                    case 2:
                        secondHandCounter++;
                        break;
                }
            }
        }

        // The product state which has more items counted is the user's favourite.
        int max = Math.max(newCounter, Math.max(reconditionedCounter, secondHandCounter));

        if (max == newCounter) {
            user.setFavouriteState(0);
        } else if (max == reconditionedCounter) {
            user.setFavouriteState(1);
        } else if (max == secondHandCounter) {
            user.setFavouriteState(2);
        }

        this.saveUser(user);
    }

    public long countTotalUsers() {
        return userRepository.count();
    }
}