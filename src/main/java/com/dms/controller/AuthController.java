package com.dms.controller;

import com.dms.dto.UserDTO;
import com.dms.entity.mysql.User;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;


    public AuthController(UserService userService,
                          JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Validated(ValidationGroups.register.class) @RequestBody UserDTO request) throws BadRequestException, MethodArgumentNotValidException {
        userService.registerUser(request);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Validated(ValidationGroups.Login.class) @RequestBody UserDTO request) throws BadRequestException {
        User user = userService.loginUser(request);
        String token = jwtUtil.generateToken(user);
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", Map.of(
                "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "email", user.getEmail(),
                "nid", user.getNationalId()
        ));
        return ResponseEntity.ok(response);
    }
}
