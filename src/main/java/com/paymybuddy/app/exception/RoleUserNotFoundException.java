package com.paymybuddy.app.exception;

public class RoleUserNotFoundException extends RuntimeException {
    public RoleUserNotFoundException(String message){
        super(String.format(message));
    }
}
