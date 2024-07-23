package com.example.projetpfe.service;

import com.example.projetpfe.model.AuthenticationResponse;
import com.example.projetpfe.model.RegistrationRequest;
import com.example.projetpfe.model.user.Token;
import com.example.projetpfe.model.user.TokenRepository;
import com.example.projetpfe.model.user.UserRepository;
import com.example.projetpfe.security.*;
import com.example.projetpfe.model.user.User;




import com.sun.tools.jconsole.JConsoleContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final UserRepository ur;
    @Autowired
    private JavaMailSender mailSender;

    // @Value("${application.mailing.frontend.activation-url}")
    //private String activationUrl;

   /* public User getclient (@RequestParam String username )
    {
        return  userRepository.findByUsername(username);
    }*/
    public User getclient (@RequestParam String email )
    {
        return  userRepository.findByEmail(email);
    }

    public RegistrationRequest register(User request) {


        User u=userRepository.findByUsername(request.getUsername());
        if(u!=null)
        {
            return new RegistrationRequest("User already exist");

        }



        User user = new User();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());

        //  user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        user.setAccountNonLocked(false);
        user.setEmailVerified(false);

        String code = generateVerificationCode();
        user.setVerificationCode(code);

        user = userRepository.save(user);

        // Envoyer l'e-mail de vérification
        sendVerificationEmail(user.getEmail(), code);


        // String accessToken = jwtService.generateAccessToken(user);
        // String refreshToken = jwtService.generateRefreshToken(user);

        //saveUserToken(accessToken, refreshToken, user);

        //return new AuthenticationResponse(accessToken, refreshToken, "User registration was successful");
        return new RegistrationRequest( "User registration was successful");


    }

    /* public AuthenticationResponse authenticate(User request) {
        authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken(
                         request.getUsername(),
                         request.getPassword()
                 )
         );

         User user= this.getclient(request.getUsername());
        // User user = userRepository.findByUsername(request.getUsername());
         String accessToken = jwtService.generateAccessToken(user);
         String refreshToken = jwtService.generateRefreshToken(user);

         //revokeAllTokenByUser(user);
         saveUserToken(accessToken, refreshToken, user);

         return new AuthenticationResponse(accessToken, refreshToken, "User login was successful");

     }*/
    public String authenticate(User request) {


       // User user= this.getclient(request.getUsername());
        User user = this.getclient(request.getEmail());
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Mot de passe incorrect");
        }
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        //revokeAllTokenByUser(user);
        saveUserToken(accessToken, refreshToken, user);

        return accessToken;
    }


    public void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllAccessTokensByUser(user.getId());
        if (validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t -> {
            t.setLoggedOut(true);
        });

        tokenRepository.saveAll(validTokens);
    }

    private void saveUserToken(String accessToken, String refreshToken, User user) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }

    public ResponseEntity refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {
        // extract the token from authorization header
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        // extract username from token
        String username = jwtService.extractUsername(token);

        // check if the user exist in database
        User user = userRepository.findByUsername(username);

        // check if the token is valid
        if (jwtService.isValidRefreshToken(token, user)) {
            // generate access token
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            revokeAllTokenByUser(user);
            saveUserToken(accessToken, refreshToken, user);

            return new ResponseEntity(new AuthenticationResponse(accessToken, refreshToken, "New token generated"), HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.UNAUTHORIZED);

    }



    //verification de signup!

    private String generateVerificationCode() {
        // Générer un code de vérification à 6 chiffres
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }

    private void sendVerificationEmail(String email, String code) {
        String subject = "Email Verification";
        String message = "Please verify your email by entering the following code:\n" + code;

        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(email);
        emailMessage.setSubject(subject);
        emailMessage.setText(message);

        mailSender.send(emailMessage);
    }


    public String verifyEmail(String username, String code) {
        User user = userRepository.findByUsername(username);
        if (user == null || !user.getVerificationCode().equals(code)) {
            return "Invalid verification code";
        }

        user.setAccountNonLocked(true); // Déverrouille le compte
        user.setEmailVerified(true); // Marque l'email comme vérifié
        userRepository.save(user);

        return "Email verified successfully";
    }

    public User getuserbyid(Integer id)
    {
        return userRepository.findById(id).orElseThrow();
    }
    public void deleteuser(Integer id)
    {
        List<Token> tokens= tokenRepository.findAllAccessTokensByUser(id);
        for(Token t : tokens)
        {
            tokenRepository.delete(t);
        }

         userRepository.deleteById(id);
    }
    /*public User updateuser(User u) {
        return ur.save(u);
    }*/

    //status offline/online
        public String status(Integer id)
        {
            User u=ur.findById(id).orElseThrow();
            List<Token> tokens=u.getTokens();
            if(tokens!=null) {
                for (Token i : tokens) {
                    if (i.isLoggedOut()) {
                        return "offline";
                    }
                    else
                        return "online";
                }
            }
    
                return "pas encore ";
    
    
    
    
        }

















}
