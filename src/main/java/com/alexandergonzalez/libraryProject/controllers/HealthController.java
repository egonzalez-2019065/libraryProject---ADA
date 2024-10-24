package com.alexandergonzalez.libraryProject.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/health")
public class HealthController {

    @GetMapping("/")
    public String checkHealth() {
        return "API is up and running!";
    }
}
