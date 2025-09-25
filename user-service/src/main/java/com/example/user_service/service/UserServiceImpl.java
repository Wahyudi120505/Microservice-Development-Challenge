package com.example.user_service.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.user_service.dto.LoginRequest;
import com.example.user_service.dto.LoginResponse;
import com.example.user_service.dto.RegisRequest;
import com.example.user_service.dto.UserResponse;
import com.example.user_service.enums.RoleEnum;
import com.example.user_service.model.User;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.security.JwtUtil;

@Service
public class UserServiceImpl implements UserService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
            EmailService emailService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public String register(RegisRequest regisRequest) {
        if (userRepository.findByEmail(regisRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email sudah terdaftar");
        }

        String verificationCode = UUID.randomUUID().toString().substring(0, 8);

        User newUser = User.builder()
                .email(regisRequest.getEmail())
                .username(regisRequest.getUsername())
                .password(passwordEncoder.encode(regisRequest.getPassword()))
                .starDate(LocalDate.now())
                .isVerified(false)
                .verificationCode(verificationCode)
                .verificationCodeExpiry(LocalDateTime.now().plusMinutes(5))
                .role(RoleEnum.STAFF)
                .build();

        userRepository.save(newUser);

        emailService.sendEmail(regisRequest.getEmail(), "Verifikasi Email Anda", verificationCode);
        return null;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Akun tidak ada/tidak terdaftar"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Email Atau Password Salah");
        }

        if (!Boolean.TRUE.equals(user.getIsVerified())) {
            throw new RuntimeException("Tidak dapat login karena belum diverifikasi");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }

        String token = jwtUtil.generateToken(user);

        return new LoginResponse(user.getId(), user.getRole().name(), user.getUsername(), token);
    }

    @Override
    public UserResponse getById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan dengan id: " + id));
        return UserResponse.builder()
            .username(user.getUsername())
            .email(user.getEmail())
            .role(user.getRole().name())
            .build();
    }
}
