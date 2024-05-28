package com.example.projetpfe.model;

import jakarta.persistence.Entity;



public class PaymentRequest {

    private String token;
    private Double amount;

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
