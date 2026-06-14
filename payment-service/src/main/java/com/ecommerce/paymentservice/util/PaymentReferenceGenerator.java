package com.ecommerce.paymentservice.util;

import java.util.UUID;

public class PaymentReferenceGenerator {

    public static String generate() {
        return "PAY-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }
}
