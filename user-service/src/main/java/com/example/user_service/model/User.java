package com.example.user_service.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.user_service.enums.RoleEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String email;
    private String password;
    private LocalDate starDate;
    private String verificationCode;
    private LocalDateTime verificationCodeExpiry;

    private Boolean isVerified;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;
}
