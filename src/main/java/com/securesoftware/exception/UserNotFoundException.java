package com.securesoftware.exception;

public class UserNotFoundException extends Exception {
    private long user_id;
    
    public UserNotFoundException(long user_id) {
        super(String.format("User with the following ID can't be found: '%s'", user_id));
    }
}
