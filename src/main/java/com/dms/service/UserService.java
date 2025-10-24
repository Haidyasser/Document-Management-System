package com.dms.service;

import com.dms.dto.UserDTO;
import com.dms.exception.BadRequestException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserService {
    void registerUser(UserDTO request) throws BadRequestException;

    String loginUser(@RequestBody UserDTO request) throws BadRequestException;
}
