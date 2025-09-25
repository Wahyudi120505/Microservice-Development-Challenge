package com.example.user_service.init;

import java.time.LocalDate;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.user_service.enums.RoleEnum;
import com.example.user_service.model.User;
import com.example.user_service.repository.UserRepository;

@Component
public class InitialDataLoader implements ApplicationRunner{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public InitialDataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.findAll().isEmpty()) {
            User admin = User.builder()
                .username("wahyudi")
                .email("admin123@gmail.com")
                .password(passwordEncoder.encode("admin123"))
                .isVerified(true)
                .starDate(LocalDate.now())
                .verificationCode(null)
                .verificationCodeExpiry(null)
                .role(RoleEnum.ADMIN)
                .build();
            userRepository.save(admin);
        }
    }
}
