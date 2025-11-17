package com.dms.service.Impl;

import com.dms.dto.UserDTO;
import com.dms.entity.mysql.User;
import com.dms.exception.BadRequestException;
import com.dms.mapper.UserMapper;
import com.dms.repository.mysql.UserRepository;
import com.dms.security.JwtUtil;
import com.dms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper,  JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public void registerUser(UserDTO userDto) throws BadRequestException {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new BadRequestException("Email already exits");
        }
        if (userRepository.existsByNationalId(userDto.getNationalId())) {
            throw new BadRequestException("National Id already exits");
        }

        // Hash the password before saving
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = userMapper.toEntity(userDto);
        userRepository.save( user);
    }

    @Override
    public User loginUser(@RequestBody UserDTO request) throws BadRequestException {
        User user = userRepository.findByEmail(request.getEmail().trim()).orElseThrow(
                () -> new BadRequestException("Invalid email or password!")
        );

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new BadRequestException("Invalid credentials!");
        }

        return user;
    }
}
