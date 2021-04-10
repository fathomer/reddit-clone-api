package com.reddit.clone.exception;

public class SubredditNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -2751903373317480026L;

    public SubredditNotFoundException(String message) {
        super(message);
    }
}
