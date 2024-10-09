package com.example.onlinechatroomdemoproject.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class FileService {
    public void uploadPictureToProfile(){

    }
    public String generateFileName(String fileName){
        Random randomSuffix = new Random();
        return fileName + randomSuffix.nextLong(10000, 99999);
    }
}
