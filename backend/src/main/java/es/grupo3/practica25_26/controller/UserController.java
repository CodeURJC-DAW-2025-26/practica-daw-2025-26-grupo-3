package es.grupo3.practica25_26.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.grupo3.practica25_26.model.Error;
import es.grupo3.practica25_26.model.Image;
import es.grupo3.practica25_26.model.Order;
import es.grupo3.practica25_26.model.Product;
import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.service.ErrorService;
import es.grupo3.practica25_26.service.ImageService;
import es.grupo3.practica25_26.service.OrderService;
import es.grupo3.practica25_26.service.ProductService;
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

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @PostMapping("/user_register")
    public String userRegister(Model model, User newUser, HttpSession session, HttpServletRequest request,
            MultipartFile imageFile, RedirectAttributes redirectAttributes)
            throws IOException, ServletException {

        Error error = userService.userRegisterCheck(newUser);

        if (error != null) {
            session.setAttribute("user_failed_register", newUser);
            return errorService.setErrorPageWithButton(model, session, error.getTitle(), error.getMessage(),
                    "Volver al registro",
                    "/signup");
        } else {
            String pass = newUser.getPassword();
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

            redirectAttributes.addFlashAttribute("successful_register", true);
            redirectAttributes.addFlashAttribute("user_name", newUser.getUserName());

            return "redirect:/";
        }
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpServletRequest request) {
        String email = request.getUserPrincipal().getName();
        User user = userService.findUserByEmail(email);
        List<Order> userOrders = user.getOrders();
        model.addAttribute("orders", userOrders);
        model.addAttribute("hasOrders", userOrders != null && !userOrders.isEmpty());
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

        String currentEmail = request.getUserPrincipal().getName();
        User currentUser = userService.findUserByEmail(currentEmail);
        Error error = userService.userUpdateCheck(userName, surname, email, address, request);

        if (error != null) {
            User failedUser = new User(userName, surname, address, email, currentUser.getPassword());
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
        String email = request.getUserPrincipal().getName();
        User currentUser = userService.findUserByEmail(email);
        Error error = userService.userPasswordUpdateCheck(newPassword, oldPassword, request);

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

    @PostMapping("profile/delete")
    public String deleteProfile(Model model, HttpServletRequest request,
            @RequestParam String currentPassword,
            @RequestParam(required = false, defaultValue = "false") boolean confirmDelete) {

        Error error = userService.userDeleteCheck(currentPassword, confirmDelete, request);
        if (error != null) {
            return errorService.setErrorPageWithButton(model, null, error.getTitle(), error.getMessage(),
                    "Volver al perfil", "/profile");
        }

        String email = request.getUserPrincipal().getName();
        User currentUser = userService.findUserByEmail(email);
        userService.deleteUserById(currentUser.getId(), email);

        // We force user logout manually to avoid problems with erased user in database
        try {
            request.logout();
        } catch (ServletException e) {
            e.printStackTrace();
        }

        return "redirect:/";
    }

    // Route to unblock users
    @PostMapping("/user/{id}/unblock")
    public String unblockUser(Model model, HttpServletRequest request, @PathVariable Long id) {
        User userToUnblock = userService.findUserById(id);
        Error error = userService.userUnblockCheck(userToUnblock);
        if (error == null) {
            userService.unblockUser(userToUnblock);
        }
        return "redirect:/user_registered_list";
    }

    // Route to block users
    @PostMapping("/user/{id}/block")
    public String blockUser(Model model, HttpServletRequest request, @PathVariable Long id) {
        User userToBlock = userService.findUserById(id);
        Error error = userService.userBlockCheck(userToBlock);
        if (error == null) {
            userService.blockUser(userToBlock);
        }
        return "redirect:/user_registered_list";

    }

    @GetMapping("/user/{id}/details")
    public String userDetails(Model model, HttpServletRequest request, @PathVariable Long id) {
        User user = userService.findUserById(id);
        if (user == null) {
            return errorService.setErrorPageWithButton(model, null, "Usuario no encontrado",
                    "No se ha encontrado el usuario con ID: " + id, "Volver a la lista de usuarios",
                    "/user_registered_list");
        }
        model.addAttribute("user", user);
        return "profile_details";
    }

    // Renders the main dashboard panel for administrators
    @GetMapping("/admin_panel")
    public String adminPanel(Model model) {
        // admins and users registered
        model.addAttribute("totalUsers", userService.countTotalUsers());
        model.addAttribute("totalProducts", productService.countTotalProducts());
        model.addAttribute("pendingOrders", orderService.countPendingOrders());
        model.addAttribute("totalAmountMoney", orderService.calculateTotalSalesAmount());

        List<Object[]> topProducts = orderService.getTopSellingProducts(PageRequest.of(0, 5));
        List<String> nombres = new ArrayList<>();
        List<Long> ventas = new ArrayList<>();

        for (Object[] row : topProducts) {
            nombres.add(row[0].toString());
            ventas.add(((Number) row[1]).longValue());
        }

        String nombresJs = nombres.isEmpty() ? "[]" : "['" + String.join("', '", nombres) + "']";
        String ventasJs = ventas.toString();

        model.addAttribute("topProductNames", nombresJs);
        model.addAttribute("topProductSales", ventasJs);

        // Get product state distribution data
        List<Product> allProducts = productService.findAll();
        long newCount = allProducts.stream().filter(p -> p.getState() == 0).count();
        long reconditionedCount = allProducts.stream().filter(p -> p.getState() == 1).count();
        long secondHandCount = allProducts.stream().filter(p -> p.getState() == 2).count();

        String pedidosChartDataJs = "[" + newCount + ", " + reconditionedCount + ", " + secondHandCount + "]";
        model.addAttribute("pedidosChartData", pedidosChartDataJs);

        return "admin_panel";
    }

    // Displays the profile settings page for administrators
    @GetMapping("/admin_profile")
    public String adminProfile() {
        return "admin_profile";
    }

    // Retrieves and displays a list of all standard registered users, excluding
    // administrators
    @GetMapping("/user_registered_list")
    public String userRegisteredList(Model model) {
        model.addAttribute("users", userService.getUsersWithoutAdmins());
        return "user_registered_list";
    }

}
