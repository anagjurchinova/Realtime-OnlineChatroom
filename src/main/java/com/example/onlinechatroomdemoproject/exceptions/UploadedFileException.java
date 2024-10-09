package com.example.onlinechatroomdemoproject.exceptions;

public class UploadedFileException extends RuntimeException{
    String message;
    public UploadedFileException(String message){
        this.message = message;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
