package com.ecommerce.paymentservice.service.serviceImpl;

import com.ecommerce.paymentservice.client.OrderClient;
import com.ecommerce.paymentservice.dto.*;
import com.ecommerce.paymentservice.entity.Payment;
import com.ecommerce.paymentservice.enums.OrderStatus;
import com.ecommerce.paymentservice.enums.PaymentStatus;
import com.ecommerce.paymentservice.exceptions.PaymentNotFoundException;
import com.ecommerce.paymentservice.mapper.PaymentMapper;
import com.ecommerce.paymentservice.repository.PaymentRespository;
import com.ecommerce.paymentservice.service.PaymentGateway;
import com.ecommerce.paymentservice.service.PaymentService;
import com.razorpay.RazorpayException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRespository paymentRespository;
    private final PaymentMapper paymentMapper;
    private final OrderClient orderClient;
    private final PaymentGateway paymentGateway;
    @Override
    public PaymentResponseDto createPayment(PaymentRequestDto paymentRequestDto) throws RazorpayException {
        OrderResponseDto orderResponseDto = orderClient.getOrder(paymentRequestDto.getOrderNumber());
        if(orderResponseDto == null) {
            throw new RuntimeException("Order Not Found");
        }
        if(orderResponseDto.getOrderStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new IllegalStateException("Payment can only be created for PENDING_PAYMENT orders");
        }
        Optional<Payment> existingPayment = paymentRespository.findByOrderNumber(paymentRequestDto.getOrderNumber());
        if(existingPayment.isPresent()) {
            throw new IllegalStateException("Payment already exists for order");
        }
        Payment payment = paymentMapper.toEntity(paymentRequestDto);
        payment.setAmount(orderResponseDto.getTotalAmount());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        GatewayOrderResponse gatewayOrderResponse = paymentGateway.createOrder(payment.getOrderNumber(), payment.getAmount());
        payment.setTransactionId(gatewayOrderResponse.getGatewayOrderId());
        return paymentMapper.toDto(paymentRespository.save(payment));
    }

    @Override
    public PaymentResponseDto getPaymentByOrderNumber(String orderNumber) {
        Payment payment = paymentRespository.findByOrderNumber(orderNumber).orElseThrow(() -> new PaymentNotFoundException("Payment Not Found"));
        return paymentMapper.toDto(payment);
    }

    @Override
    public PaymentResponseDto updatePaymentStatus(Long paymentId, PaymentStatusUpdateRequest statusUpdateRequest) {
        Payment payment = paymentRespository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException("Payment Not Found with Id : " + paymentId));
        payment.setPaymentStatus(statusUpdateRequest.getPaymentStatus());
        return paymentMapper.toDto(payment);
    }

    @Override
    public PaymentResponseDto markPaymentSuccess(Long paymentId) {
        Payment payment = paymentRespository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found"));

        payment.setPaymentStatus(PaymentStatus.SUCCESS);

        paymentRespository.save(payment);

        orderClient.confirmOrder(payment.getOrderNumber());

        return paymentMapper.toDto(payment);
    }

    @Override
    public PaymentResponseDto markPaymentFailure(Long paymentId) {
        Payment payment = paymentRespository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException("Payment not found"));
        payment.setPaymentStatus(PaymentStatus.FAILED);
        paymentRespository.save(payment);

        orderClient.failOrder(payment.getOrderNumber());

        return paymentMapper.toDto(payment);
    }
}
