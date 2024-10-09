package com.example.onlinechatroomdemoproject.service;

import com.example.onlinechatroomdemoproject.exceptions.*;
import com.example.onlinechatroomdemoproject.model.User;
import com.example.onlinechatroomdemoproject.repository.InMemoryUserRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService{
    private final InMemoryUserRepo userRepository;

    public UserServiceImpl(InMemoryUserRepo userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public User login(String usernameOrEmail, String password) {
        if(usernameOrEmail==null || password == null || usernameOrEmail.isEmpty() || password.isEmpty()){
            throw new InvalidInputException("Please fill in all of the boxes.");
        }

        Optional<User> user = userRepository.search(usernameOrEmail, password);
        if(user.isEmpty()) throw new InvalidInputException("Your login information is not valid.");

        return user.get();
    }

    // TODO: 16.8.2024 Password must be longer than 8 chars. 
    @Override
    public User signUp(String email, String username, String password,
                       String repeatPassword, String phoneNumber)
    {

        if(email == null || email.isEmpty() || username == null || username.isEmpty() ||
                password == null || password.isEmpty() || repeatPassword == null || repeatPassword.isEmpty())
            throw new InvalidInputException("Please fill in all of the boxes.");

        if(!password.equals(repeatPassword))
            throw new InvalidInputException("Please enter matching passwords.");

        if(userRepository.searchByUsername(username).isPresent())
            throw new InvalidInputException(String.format("Username %s is already taken.",username));

        if(userRepository.searchByEmail(email).isPresent())
            throw new InvalidInputException("Account e-mail already exists.");

        if(!email.contains("@") || !Character.isAlphabetic(email.charAt(0)) || email.contains(" ")){
            throw new InvalidInputException("Please enter a valid e-mail.");
        }

        // TODO: 15.7.2024 check taken phone number and phone number format

        return new User(email, username, password, phoneNumber);
    }

    @Override
    public boolean compareCodes(String cod1, String cod2) {
        return cod1.equals(cod2);
    }

    @Override
    public String generateUsername(String firstName, String lastName) {
        String baseName = (firstName + lastName).toLowerCase().replaceAll("\\s+", "");
        String randomSuffix = String.format("%04d", new Random().nextInt(10000));
        String generatedUsername = baseName + randomSuffix;

        //ensures the generated username is available.
        if(userRepository.searchByUsername(generatedUsername).isPresent())
            generateUsername(firstName, lastName);

        return generatedUsername;
    }

    @Override
    public void changeProfilePicture(MultipartFile file, Path uploadPath, User user) throws IOException {
        if(file == null || file.isEmpty())
            throw new UploadedFileException("Please upload a file.");

        byte[] fileBytes = file.getBytes();
        System.out.println(file.getOriginalFilename());

        String fileType = Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[1];
        String filePath = uploadPath + "\\" + user.getUsername() + "." + fileType;
        Path fileUploadPath = Paths.get(filePath);

        Files.write(fileUploadPath, fileBytes);
        user.setProfilePicturePath(filePath);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword, String repeatPassword) {
        // TODO: 15.7.2024
    }

    @Override
    public void changeUsername(String newUsername, String password, String repeatPassword) {
        // TODO: 15.7.2024
    }
}
