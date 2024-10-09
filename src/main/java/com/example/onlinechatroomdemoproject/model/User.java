package com.example.onlinechatroomdemoproject.model;

import lombok.Data;
import lombok.Setter;

@Data
public class User {

    private static long idSeed = 10000;
    private long id;
    private String email;
    private String username;
    private String password;
    private String phoneNumber;
    private String defaultPicturePath;

    @Setter
    private String profilePicturePath;

    public User(String email, String username, String password, String phoneNumber) {
        this.id = idSeed++;
        this.email = email;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.defaultPicturePath = "C:\\Users\\ANA\\Desktop\\SPRING PROJECT\\online-chatroom-demoProject\\src\\main\\resources\\static\\usr_pfps\\default.png";
    }

}
