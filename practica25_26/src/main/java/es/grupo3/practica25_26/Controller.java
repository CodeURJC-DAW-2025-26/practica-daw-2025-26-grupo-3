package es.grupo3.practica25_26;

import org.springframework.web.bind.annotation.GetMapping;

import ch.qos.logback.core.model.Model;

public class Controller {
    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

}
