package com.example.bookingservice.exception;

import java.util.NoSuchElementException;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
