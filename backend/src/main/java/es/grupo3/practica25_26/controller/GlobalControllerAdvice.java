package es.grupo3.practica25_26.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import es.grupo3.practica25_26.model.User;
import es.grupo3.practica25_26.repository.UserRepository;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;

@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    UserRepository userRepository;

    @ModelAttribute
    public void addCsrfToken(HttpServletRequest request, Model model) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (csrfToken != null) {
            model.addAttribute("_csrf", csrfToken);
        }
    }

    @ModelAttribute
    public void addUserAttributes(Model model, HttpServletRequest request) {
        if (request.getUserPrincipal() != null) {
            String email = request.getUserPrincipal().getName();
            User user = userRepository.findDistinctByEmail(email).orElse(null);

            if (user != null) {
                model.addAttribute("user", user);
                model.addAttribute("user_logged", true);
                return;
            }
        }

        model.addAttribute("user_logged", false);
    }
}
