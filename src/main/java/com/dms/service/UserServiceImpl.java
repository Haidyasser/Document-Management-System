package com.dms.service;

import com.dms.dto.LoginRequest;
import com.dms.entity.User;
import com.dms.exception.UserLoginException;
import com.dms.exception.UserRegistrationException;
import com.dms.repository.UserRepository;
import com.dms.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String registerUser(User request) throws UserRegistrationException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserRegistrationException("Email already exits");
        }
        if (userRepository.existsByNationalId(request.getNationalId())) {
            throw new UserRegistrationException("National Id already exits");
        }

        // Hash the password before saving
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(request);
        return "User registered successfully";
    }

    @Override
    public String loginUser(@RequestBody LoginRequest request) throws UserLoginException {
        JwtUtil jwtUtil = new JwtUtil();
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail().trim());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
                throw new UserLoginException("Invalid credentials!");
            }

            return jwtUtil.generateToken(user.getEmail());
        }else{
            throw new UserLoginException("Invalid email or password!");

        }
    }
}
