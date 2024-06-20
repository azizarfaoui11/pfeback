package com.example.projetpfe.service;

import com.example.projetpfe.model.user.PasswordResetToken;
import com.example.projetpfe.model.user.PasswordResetTokenRepository;
import com.example.projetpfe.model.user.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Random;

@Service

@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepo;
    @Autowired
    private JavaMailSender mailSender;

    public void createPasswordResetTokenForUser(String email, String token) {
        PasswordResetToken myToken = new PasswordResetToken();
        myToken.setToken(token);
        myToken.setEmail(email);
        myToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        //String verificationCode = String.format("%06d", new Random().nextInt(999999));
        //myToken.setVerificationCode(verificationCode);
        tokenRepo.save(myToken);
       sendEmail(email, token );
        //sendEmail(email,token);

    }

    private void sendEmail(String email, String token) {
        String encodedToken = Base64.getEncoder().encodeToString(token.getBytes());

        String url = "http://localhost:4200/reset-password?token=" + encodedToken ;

        String message = "Cliquez sur le lien pour réinitialiser votre mot de passe: " + url;

       // "\n Votre code de vérification est: " + verificationCode;
        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(email);
        emailMessage.setSubject("Réinitialisation du mot de passe");
        emailMessage.setText(message);
        mailSender.send(emailMessage);
    }
}
