package com.example.projetpfe.controller;

import com.example.projetpfe.model.RegistrationRequest;
import com.example.projetpfe.model.user.Token;
import com.example.projetpfe.model.user.User;
import com.example.projetpfe.model.user.UserRepository;
import com.example.projetpfe.model.user.Usersession;
import com.example.projetpfe.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequiredArgsConstructor
//@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService service;
    @Autowired
    private final UserRepository userrepo;

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

  /*  @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        // Récupérer le token du header Authorization
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization header is missing or invalid");
        }

        String token = authorizationHeader.substring(7);
        try {
            service.logout(token);
            return ResponseEntity.ok("Logout successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
*/

    //hedhi logout ama lezemni nwali nabaath accestoken fel headers mtaa requette angular mech entite USER kemla c est pas logique
    @PostMapping("/revoke-tokens")
    public void revokeAllTokens(@RequestBody User user) {
        service.revokeAllTokenByUser(user);
        // Vous pouvez choisir de retourner un code de statut ou un message de confirmation si nécessaire
    }

    @PostMapping("/refresh_token")
    public ResponseEntity refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return service.refreshToken(request, response);
    }

    //@PreAuthorize("hasAuthority('USER')")
       /* @PostMapping("/ping")
        public ResponseEntity<?> test(@RequestBody String email) {
            try {
                List<Token> tokens = service.gettoken(email);
                return ResponseEntity.ok(tokens);
            } catch (UsernameNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }*/

    /*@PostMapping("/pingg")
    public List<Token> testt(@RequestBody String email) {



          User u =    service.testFindByUsername(email);
          return u.getTokens();

    }*/


    @GetMapping("/me")
    public ResponseEntity<Usersession> getCurrentUser(Principal principal) {
        User user =userrepo.findByFirstname(principal.getName());

        Usersession usersess = new Usersession(user.getFirstname(), user.getId());
       //User prinuser= user.getFirstname();
        return ResponseEntity.ok(usersess);
    }

    //   *******for backend dashboard ******** //

    @GetMapping("/getallusers")
    public List<User> getusers ()
    {

        List users = userrepo.findAll();
        return users ;

    }

    @GetMapping("/userbyemail")
    public User getuserbyemail(@RequestParam String email)
    {
        User u=userrepo.findByEmail(email);
        return u;
    }





}
