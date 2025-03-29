package com.example.demo.exceptions;

public class SalleNotAvailableException extends RuntimeException {
    public SalleNotAvailableException() {
        super(
                "La salle n'ést pas disponible dans ce temps!"
        );
    }
}
