package com.app.backend.customException;

public class PendingTransactionException extends Exception{
    public PendingTransactionException(String message) {
        super(message);
    }
}
