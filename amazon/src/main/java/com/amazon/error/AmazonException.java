package com.amazon.error;

import lombok.Getter;

@Getter
public class AmazonException extends RuntimeException {
    public AmazonException(String message) {
        super(message);
    }

    public AmazonException(String message, Throwable cause) {
        super(message, cause);
    }
}
