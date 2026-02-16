package es.grupo3.practica25_26.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;
import es.grupo3.practica25_26.model.Error;

@Service
public class ErrorService {

    @Autowired
    UserService userService;

    public String setErrorPageWithButton(Model model, HttpSession session, String title, String message,
            String buttonName, String buttonLink) {
        Error error = new Error(title, message);

        userService.getUserNavInfo(model, session);

        model.addAttribute("error", error);
        model.addAttribute("extraButton", true);
        model.addAttribute("buttonName", buttonName);
        model.addAttribute("buttonLink", buttonLink);

        return "error";
    }

    public String setErrorPage(Model model, HttpSession session, String title, String message) {
        Error error = new Error(title, message);

        userService.getUserNavInfo(model, session);

        model.addAttribute("error", error);
        model.addAttribute("extraButton", false);
        return "error";
    }

}
