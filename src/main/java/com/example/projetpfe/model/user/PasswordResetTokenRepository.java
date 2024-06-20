package com.example.projetpfe.model.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Integer> {
    PasswordResetToken findByToken(String token);
    PasswordResetToken findByVerificationCode(String code);

}
