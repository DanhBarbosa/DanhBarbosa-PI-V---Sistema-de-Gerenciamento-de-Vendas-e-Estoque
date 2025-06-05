package br.senac.tads.pi.PI_IV.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {


    @GetMapping("/")
    public String home() {
        return "redirect:/login.html";
    }
}
