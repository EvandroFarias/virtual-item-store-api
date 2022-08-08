package com.app.backend.customException;

public class AlreadyRegisteredException extends Exception{
    public AlreadyRegisteredException(String message) {
        super(message);
    }
}
