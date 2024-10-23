package com.alexandergonzalez.libraryProject.exceptions.exception;

public class InvalidJWT extends RuntimeException {
    public InvalidJWT(String message) {
        super(message);
    }
}
