package com.example.projetpfe.controller;

import com.example.projetpfe.model.RegistrationRequest;
import com.example.projetpfe.model.user.Token;
import com.example.projetpfe.model.user.User;
import com.example.projetpfe.model.user.UserRepository;
import com.example.projetpfe.model.user.Usersession;
import com.example.projetpfe.service.AuthenticationService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequiredArgsConstructor
//@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService service;
    @Autowired
    private final UserRepository userrepo;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public ResponseEntity<RegistrationRequest> register(
            @RequestBody User request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam("username") String username, @RequestParam("code") String code) {
        String result = service.verifyEmail(username, code);
        if (result.equals("Email verified successfully")) {
            return ResponseEntity.ok().body("{\"status\": \"success\", \"message\": \"Email verified successfully\"}");
        } else {
            return ResponseEntity.badRequest().body("{\"status\": \"error\", \"message\": \"" + result + "\"}");
        }
    }


    @GetMapping("/getuser")
    public User getclient(@RequestParam String email)
    {
        return service.getclient(email);
    }


    @PostMapping("/login")
    public String login(
            @RequestBody User request)
    {
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
        //User user = userrepo.findByEmail(principal.getName());

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
    @GetMapping("/getuser/{id}")
       public User showuser(@PathVariable("id") Integer idu)
    {
        return service.getuserbyid(idu);
    }

    @GetMapping("/userbyemail")
    public User getuserbyemail(@RequestParam String email)
    {
        User u=userrepo.findByEmail(email);
        return u;
    }




    @DeleteMapping("/delete/{id}")
    public void deleteuser(@PathVariable("id") Integer idu)
    {
         service.deleteuser(idu);
    }

    @PutMapping("/updateuser/{id}")
    public User updateuser(@PathVariable("id") Integer id, @RequestBody User user)
    {

        User u = userrepo.findById(id).orElseThrow();
        u.setFirstname(user.getFirstname());
        u.setLastname((user.getLastname()));
        u.setUsername(user.getUsername());
        u.setEmail(user.getEmail());
        u.setPassword(passwordEncoder.encode(user.getPassword()));
        u.setRole(user.getRole());

        return userrepo.save(u);
    }

   /* @PutMapping("/updateuser")

        public User updateuser(@RequestBody User user)
    {
         return service.updateuser(user);
    }*/

    @PostMapping("/adduser")
    public User addUser(@RequestBody User user)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userrepo.save(user);
    }

    @PostMapping("/status/{id}")
            public ResponseEntity<Map<String,String>>status(@PathVariable Integer id)
    {
        String status=service.status(id);
        Map<String,String> response= new HashMap<>();
        response.put("status",status);
    return ResponseEntity.ok(response);
    }








}
