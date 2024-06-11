package com.example.projetpfe.controller;

import com.example.projetpfe.model.RegistrationRequest;
import com.example.projetpfe.model.user.User;
import com.example.projetpfe.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequiredArgsConstructor
//@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<RegistrationRequest> register(
            @RequestBody User request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    /*@PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody User request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }*/
    @GetMapping("/getuser")
    public User getclient(@RequestParam String username)
    {
        return service.getclient(username);
    }
    @PostMapping("/login")
    public String login(
            @RequestBody User request
    ) {
        return (service.authenticate(request));
    }
    @PostMapping("/refresh_token")
    public ResponseEntity refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return service.refreshToken(request, response);
    }

    //@PreAuthorize("hasAuthority('USER')")
    @GetMapping("/ping")
    public String test() {
        try {
            return "Welcome";
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }


}
