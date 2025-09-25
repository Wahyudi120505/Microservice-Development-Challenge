package com.example.user_service.service;

import com.example.user_service.dto.LoginRequest;
import com.example.user_service.dto.LoginResponse;
import com.example.user_service.dto.RegisRequest;
import com.example.user_service.dto.UserResponse;

public interface UserService {
    String register (RegisRequest regisRequest);
    LoginResponse login(LoginRequest loginRequest);
    UserResponse getById(Integer id);

}
