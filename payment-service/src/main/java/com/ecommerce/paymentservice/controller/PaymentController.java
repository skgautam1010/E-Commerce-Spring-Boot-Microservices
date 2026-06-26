package com.ecommerce.paymentservice.controller;

import com.ecommerce.paymentservice.dto.PaymentRequestDto;
import com.ecommerce.paymentservice.dto.PaymentResponseDto;
import com.ecommerce.paymentservice.dto.PaymentStatusUpdateRequest;
import com.ecommerce.paymentservice.service.PaymentService;
import com.razorpay.RazorpayException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<PaymentResponseDto> createPayment(@RequestBody @Valid PaymentRequestDto requestDto) throws RazorpayException {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayment(requestDto));
    }

    @GetMapping("/order/{orderNumber}")
    public ResponseEntity<PaymentResponseDto> getPaymentByOrderNumber(@PathVariable String orderNumber) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderNumber(orderNumber));
    }

    @PutMapping("/{paymentId}/status")
    public ResponseEntity<PaymentResponseDto> updatePaymentStatus(@PathVariable Long paymentId, @RequestBody @Valid PaymentStatusUpdateRequest request) {
        return ResponseEntity.ok(paymentService.updatePaymentStatus(paymentId, request));
    }

    /*@PostMapping("/{paymentId}/success")
    public ResponseEntity<PaymentResponseDto> markSuccess(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.markPaymentSuccess(paymentId));
    }

    @PostMapping("/{paymentId}/fail")
    public ResponseEntity<PaymentResponseDto> markFailure(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.markPaymentFailure(paymentId));
    }*/
}
