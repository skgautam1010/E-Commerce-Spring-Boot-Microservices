package com.ecommerce.inventory.exceptions;

public class InventoryException extends RuntimeException{
    public InventoryException(String skuCode) {
        super("Inventory Not found for " + skuCode);
    }


}
