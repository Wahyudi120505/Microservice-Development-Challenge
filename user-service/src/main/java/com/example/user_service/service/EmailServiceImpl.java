package com.example.user_service.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.example.user_service.model.User;
import com.example.user_service.repository.UserRepository;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private UserRepository userRepository;

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Override
    public void sendEmail(String to, String subject, String code) {
        try {
            String emailBody = loadVerificationTemplate(to, code);

            Email from = new Email("wahyutjg000@gmail.com");
            Email recipient = new Email(to);
            Content content = new Content("text/html", emailBody);
            Mail mail = new Mail(from, subject, recipient, content);

            SendGrid sendGrid = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sendGrid.api(request);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Gagal mengirim email verifikasi", e);
        }
    }

    @Override
    public void verifyEmail(String code) {
        User user = userRepository.findUserByVerificationCode(code)
                .orElseThrow(() -> new RuntimeException("Kode verifikasi tidak valid."));

        if (user.getIsVerified()) {
            throw new RuntimeException("Email sudah diverifikasi sebelumnya.");
        }

        if (user.getVerificationCodeExpiry() != null &&
                LocalDateTime.now().isAfter(user.getVerificationCodeExpiry())) {
            throw new RuntimeException("Kode verifikasi sudah kadaluarsa.");
        }

        user.setIsVerified(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiry(null);
        userRepository.save(user);
    }

    private String loadVerificationTemplate(String email, String code) throws IOException {
        ClassPathResource resource = new ClassPathResource("template/verification-email.html");
        String content = Files.readString(resource.getFile().toPath(), StandardCharsets.UTF_8);
        return content.replace("{{nama}}", email)
                .replace("{{code}}", code);
    }

}
