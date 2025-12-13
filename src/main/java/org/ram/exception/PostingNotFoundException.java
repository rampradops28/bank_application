package org.ram.exception;

public class PostingNotFoundException extends RuntimeException {
    public PostingNotFoundException(String message) {
        super(message);
    }
}