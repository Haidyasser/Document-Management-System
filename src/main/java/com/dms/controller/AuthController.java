package com.dms.controller;

import com.dms.dto.UserDTO;
import com.dms.exception.BadRequestException;
import com.dms.security.JwtUtil;
import com.dms.service.UserService;
import com.dms.validation.ValidationGroups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    public ResponseEntity<?> registerUser(@Validated(ValidationGroups.register.class) @RequestBody UserDTO request) throws BadRequestException, MethodArgumentNotValidException {
        userService.registerUser(request);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Validated(ValidationGroups.Login.class) @RequestBody UserDTO request) throws BadRequestException {
        var token = userService.loginUser(request);
        return ResponseEntity.ok(token);
    }
}
