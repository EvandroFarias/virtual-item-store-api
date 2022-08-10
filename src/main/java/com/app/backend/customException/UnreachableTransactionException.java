package com.app.backend.customException;

public class UnreachableTransactionException extends Exception{
    public UnreachableTransactionException(String message) {
        super(message);
    }
}
