package com.ecommerce.paymentservice.service.serviceImpl;

import com.ecommerce.paymentservice.dto.PaymentRequestDto;
import com.ecommerce.paymentservice.dto.PaymentResponseDto;
import com.ecommerce.paymentservice.dto.PaymentStatusUpdateRequest;
import com.ecommerce.paymentservice.entity.Payment;
import com.ecommerce.paymentservice.enums.PaymentStatus;
import com.ecommerce.paymentservice.exceptions.PaymentNotFoundException;
import com.ecommerce.paymentservice.mapper.PaymentMapper;
import com.ecommerce.paymentservice.repository.PaymentRespository;
import com.ecommerce.paymentservice.service.PaymentService;
import com.ecommerce.paymentservice.util.TransactionGenerator;
import jakarta.transaction.Transaction;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRespository paymentRespository;
    private final PaymentMapper paymentMapper;

    @Override
    public PaymentResponseDto createPayment(PaymentRequestDto paymentRequestDto) {
        Payment payment = paymentMapper.toEntity(paymentRequestDto);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setTransactionId(TransactionGenerator.generate());
        return paymentMapper.toDto(paymentRespository.save(payment));
    }

    @Override
    public PaymentResponseDto getPaymentByOrderId(String orderId) {
        Payment payment = paymentRespository.findByOrderId(orderId).orElseThrow(() -> new PaymentNotFoundException("Payment Not Found"));
        return paymentMapper.toDto(payment);
    }

    @Override
    public PaymentResponseDto updatePaymentStatus(Long paymentId, PaymentStatusUpdateRequest statusUpdateRequest) {
        Payment payment = paymentRespository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException("Payment Not Found with Id : " + paymentId));
        payment.setPaymentStatus(statusUpdateRequest.getPaymentStatus());
        return paymentMapper.toDto(payment);
    }
}
