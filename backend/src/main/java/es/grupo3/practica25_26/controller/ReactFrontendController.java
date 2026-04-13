package es.grupo3.practica25_26.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReactFrontendController {

    @GetMapping({ "/new", "/new/" })
    public String reactRoot() {
        return "forward:/new/index.html";
    }

    @GetMapping("/**/{path:[^\\.]*}")
    public String reactRoute() {
        return "forward:/new/index.html";
    }
}
