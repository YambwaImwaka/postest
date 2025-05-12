package com.example.Spring.boot.POS.System.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException() {
        super();
    }
    public InsufficientStockException(String message) {
        super(message);
    }


}
