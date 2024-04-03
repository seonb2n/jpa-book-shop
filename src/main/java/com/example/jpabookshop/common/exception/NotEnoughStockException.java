package com.example.jpabookshop.common.exception;

public class NotEnoughStockException extends RuntimeException {

    public NotEnoughStockException(String message) {
        super(message);
    }
}
