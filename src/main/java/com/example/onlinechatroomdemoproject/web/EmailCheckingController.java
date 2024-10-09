package com.example.onlinechatroomdemoproject.web;

import com.example.onlinechatroomdemoproject.model.User;
import com.example.onlinechatroomdemoproject.repository.InMemoryUserRepo;
import com.example.onlinechatroomdemoproject.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/email")
public class EmailCheckingController {
    static String code;
    UserService userService;
    InMemoryUserRepo userRepo;

    public EmailCheckingController(UserService userService, InMemoryUserRepo userRepo){
        this.userService = userService;
        this.userRepo = userRepo;
    }


    @GetMapping
    public String emailCheckingPage(@RequestParam(required = false) String error,
                                    @RequestParam(required = false) String code,
                                    Model model)
    {
        if(error != null && !error.isEmpty()){
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        if(code != null && !code.isEmpty()) EmailCheckingController.code = code;

        return "email-checking";
    }

    @PostMapping
    public String checkCode(@RequestParam String fldOne,
                            @RequestParam String fldTwo,
                            @RequestParam String fldThree,
                            @RequestParam String fldFour,
                            @RequestParam String fldFive,
                            @SessionAttribute("user") User user,
                            @SessionAttribute("codeSentAt") LocalDateTime codeSentAt
    )
    {
        String userCode = fldOne + fldTwo + fldThree + fldFour + fldFive;
        LocalDateTime timeNow = LocalDateTime.now();

        if(codeSentAt != null) {
            Duration duration = Duration.between(codeSentAt, timeNow);
            long secondsPassed = duration.getSeconds();

            if(secondsPassed > 60){
                return "redirect:/email?error=Your code has expired. Please generate a new code.&code=-1";
            }
        }

        if(userService.compareCodes(userCode, code)){
            userRepo.addOrUpdate(user);
            return "test-file-upload";
        }
        return "redirect:/email?error=The provided code is invalid.";
    }
}
