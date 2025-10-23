package com.dms.controller;

import com.dms.dto.LoginRequest;
import com.dms.entity.User;
import com.dms.exception.UserLoginException;
import com.dms.exception.UserRegistrationException;
import com.dms.security.JwtUtil;
import com.dms.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public AuthController(UserService userService,
                          JwtUtil jwtUtil) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User request) throws UserRegistrationException {
        request.setId(UUID.randomUUID().toString());
        userService.registerUser(request);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Validated @RequestBody LoginRequest request) throws UserLoginException {

//        if (userOpt.isEmpty()) {
//            return ResponseEntity.status(401).body("Invalid credentials!");
//        }
//
//        User user = userOpt.get();
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            return ResponseEntity.status(401).body("Invalid credentials!");
//        }

        var token = userService.loginUser(request);
        return ResponseEntity.ok(token);
    }
}
