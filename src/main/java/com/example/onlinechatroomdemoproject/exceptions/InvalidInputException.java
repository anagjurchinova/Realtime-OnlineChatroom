package com.example.onlinechatroomdemoproject.exceptions;

public class InvalidInputException extends RuntimeException{
    String input;
    public InvalidInputException(String input){
        this.input = input;
    }

    @Override
    public String getMessage() {
        return input;
    }
}
