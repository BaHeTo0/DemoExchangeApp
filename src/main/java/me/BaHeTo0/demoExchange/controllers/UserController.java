package me.BaHeTo0.demoExchange.controllers;

import me.BaHeTo0.demoExchange.models.entities.User;
import me.BaHeTo0.demoExchange.models.request.LoginUserRequest;
import me.BaHeTo0.demoExchange.models.request.RegisterUserRequest;
import me.BaHeTo0.demoExchange.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterUserRequest request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginUserRequest request) {
        return ResponseEntity.ok(userService.loginUser(request));
    }
}
