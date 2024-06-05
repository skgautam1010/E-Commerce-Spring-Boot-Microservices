package com.ecommerce.paymentservice.util;

import java.util.UUID;

public class TransactionGenerator {

    public static String generate() {
        return "txn-" + UUID.randomUUID();
    }
}
