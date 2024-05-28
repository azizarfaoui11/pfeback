package com.example.projetpfe.controller;

import com.example.projetpfe.model.ChargeResponse;
import com.example.projetpfe.model.PaymentRequest;
import com.example.projetpfe.model.StripeClient;
import com.stripe.model.Charge;
import org.springframework.http.ResponseEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/payment")
public class PaymentController {
    private StripeClient stripeClient;

    @Autowired
    public PaymentController(StripeClient stripeClient) {
        this.stripeClient = stripeClient;
    }

    @PostMapping("/charge")
    public ResponseEntity<ChargeResponse> chargeCard(@RequestBody PaymentRequest paymentRequest) throws Exception {
        String token = paymentRequest.getToken();
        Double amount = paymentRequest.getAmount();
        Charge charge = this.stripeClient.chargeCreditCard(token, amount);

        // Convert Charge to ChargeResponse
        ChargeResponse chargeResponse = new ChargeResponse();
        chargeResponse.setId(charge.getId());
        chargeResponse.setAmount(charge.getAmount());
        chargeResponse.setCurrency(charge.getCurrency());
        chargeResponse.setStatus(charge.getStatus());

        return ResponseEntity.ok(chargeResponse);
    }
}
