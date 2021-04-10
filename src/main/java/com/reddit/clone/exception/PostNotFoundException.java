package com.reddit.clone.exception;


public class PostNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -2751903373317480056L;

    public PostNotFoundException(String message) {
        super(message);
    }
}
