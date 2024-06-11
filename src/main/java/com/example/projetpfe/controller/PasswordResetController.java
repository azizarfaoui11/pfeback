package com.example.projetpfe.controller;

import com.example.projetpfe.model.user.PasswordResetTokenRepository;
import com.example.projetpfe.model.user.User;
import com.example.projetpfe.model.user.UserRepository;
import com.example.projetpfe.model.user.PasswordResetToken;

import com.example.projetpfe.service.PasswordResetService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@AllArgsConstructor
@RequestMapping("/password-reset")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;  // Assurez-vous d'avoir ce dépôt pour les utilisateurs

    private final PasswordEncoder passwordEncoder;


    @PostMapping("/request")
    public ResponseEntity<String> resetPasswordrequest(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        // Vérifiez si l'utilisateur existe
        if (userRepository.findByEmail(email) != null) {
            String token = UUID.randomUUID().toString();
            passwordResetService.createPasswordResetTokenForUser(email, token);
            return ResponseEntity.ok("Un email de réinitialisation a été envoyé.");
        } else {
            return ResponseEntity.badRequest().body("L'email n'existe pas.");
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        PasswordResetToken passwordResetToken = tokenRepository.findByToken(token);

        if (passwordResetToken == null || passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Le lien de réinitialisation est invalide ou a expiré.");
        }

        // Réinitialiser le mot de passe de l'utilisateur
        User user = userRepository.findByEmail(passwordResetToken.getEmail());
        // user.setPassword(newPassword); // N'oubliez pas de hacher le mot de passe
        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
        return ResponseEntity.ok("Mot de passe réinitialisé avec succès.");
    }
}


