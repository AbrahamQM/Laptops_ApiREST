package com.example.ej_Spring_rest_data.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String saludo(){
        return "Hola desde HelloController";
    }
}
