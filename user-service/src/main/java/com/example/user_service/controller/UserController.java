package com.example.user_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.user_service.dto.GenericResponse;
import com.example.user_service.dto.LoginRequest;
import com.example.user_service.dto.RegisRequest;
import com.example.user_service.service.EmailService;
import com.example.user_service.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisRequest request) {
        try {
            return ResponseEntity.ok().body(GenericResponse.success(userService.register(request),
                    "Silakan periksa email Anda untuk verifikasi."));
        } catch (ResponseStatusException e) {
            log.info("Registrasi gagal: {}", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error saat registrasi: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(GenericResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<Object> verifyUser(@RequestParam String code) {
        try {
            emailService.verifyEmail(code);
            return ResponseEntity.ok(GenericResponse.success(null, "Email berhasil diverifikasi. Silakan login."));
        } catch (ResponseStatusException e) {
            log.info("Verifikasi gagal: {}", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error saat verifikasi email: {}", e.getMessage());
            return ResponseEntity.badRequest().body(GenericResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok().body(GenericResponse.success(userService.login(request), "Login berhasil"));
        } catch (ResponseStatusException e) {
            log.info("Login gagal: {}", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error saat login: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }

    @GetMapping("user/{id}")
    public ResponseEntity<Object> getUserId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok().body(GenericResponse.success(userService.getById(id), "User di temukan"));
        } catch (ResponseStatusException e) {
            log.info("Login gagal: {}", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(GenericResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error saat login: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(GenericResponse.error(e.getMessage()));
        }
    }
}
