package com.reddit.clone.exception;

public class SpringRedditException extends RuntimeException {
    private static final long serialVersionUID = 1669473855418353963L;

    public SpringRedditException(String message) {
        super(message);
    }
}