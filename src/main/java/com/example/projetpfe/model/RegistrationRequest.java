package com.example.projetpfe.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationRequest {


    @JsonProperty("message")
    private String message;

    public RegistrationRequest(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}

