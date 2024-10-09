package com.example.onlinechatroomdemoproject.web;

import com.example.onlinechatroomdemoproject.model.User;
import com.example.onlinechatroomdemoproject.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/upload")
@SessionAttributes("user")
public class FileUploadController {
    final String FOLDER_PATH;
    final UserService userService;

    public FileUploadController(UserService userService){
        FOLDER_PATH = "C:\\Users\\ANA\\Desktop\\SPRING PROJECT\\online-chatroom-demoProject\\src\\main\\resources\\static\\usr_pfps";
        this.userService = userService;
    }

    @GetMapping()
    public String uploadPage(){
        return "test-file-upload";
    }

    @PostMapping("/profile-picture")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam(required = false, name = "file") MultipartFile file,
                                                       Model model) throws IOException {
        Path uploadPath = Paths.get(FOLDER_PATH);
        User user = (User) model.getAttribute("user");
        Long userId = user != null ? user.getId() : 0;

        System.out.println(userId);

        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        if(file == null || file.isEmpty()){
            return ResponseEntity.badRequest().body("Please select a file to upload.");
        }
        userService.changeProfilePicture(file, uploadPath, user);
        return ResponseEntity.ok().body("File: " + file.getOriginalFilename() + " was uploaded successfully.");
    }
}
