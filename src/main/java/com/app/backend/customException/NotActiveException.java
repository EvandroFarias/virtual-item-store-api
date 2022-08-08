package com.app.backend.customException;

public class NotActiveException extends Exception{

    public NotActiveException(String message) {
        super(message);
    }
}
