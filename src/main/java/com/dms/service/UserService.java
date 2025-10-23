package com.dms.service;

import com.dms.dto.LoginRequest;
import com.dms.entity.User;
import com.dms.exception.UserLoginException;
import com.dms.exception.UserRegistrationException;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserService {
    String registerUser(User request) throws UserRegistrationException;

    String loginUser(@RequestBody LoginRequest request) throws UserLoginException;
}
