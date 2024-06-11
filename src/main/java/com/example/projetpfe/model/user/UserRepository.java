package com.example.projetpfe.model.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByUsername(String username);
    public User findByPassword(String password);
    public User findByEmail(String email);

    // public User findByFirstname(String firstname);


}