package com.example.onlinechatroomdemoproject.service;
import com.example.onlinechatroomdemoproject.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public interface UserService {
    User login(String usernameOrEmail, String password);


    User signUp(String email, String username, String password,
                String repeatPassword, String phoneNumber);

    boolean compareCodes(String cod1, String cod2);

    String generateUsername(String firstName, String lastName);

    void changeProfilePicture(MultipartFile file, Path uploadPath, User user) throws IOException;
    void changeUsername(String newUsername, String password, String repeatPassword);
    void changePassword(String oldPassword, String newPassword, String repeatPassword);
}
