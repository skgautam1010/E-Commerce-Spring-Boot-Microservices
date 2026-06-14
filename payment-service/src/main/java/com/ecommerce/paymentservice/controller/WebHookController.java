package com.ecommerce.paymentservice.controller;

import com.ecommerce.paymentservice.service.RazorPayWebHookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class WebHookController {
    private final RazorPayWebHookService razorPayWebHookService;

    @PostMapping("/webhook/razorpay")
    public ResponseEntity<Void> handleWebHook(@RequestHeader("X-Razorpay-Signature") String signature, @RequestBody String payload) {
        razorPayWebHookService.processWebHook(signature, payload);
        return ResponseEntity.ok().build();
    }
}
