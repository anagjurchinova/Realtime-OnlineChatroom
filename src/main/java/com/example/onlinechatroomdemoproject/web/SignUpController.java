package com.example.onlinechatroomdemoproject.web;

import com.example.onlinechatroomdemoproject.exceptions.InvalidInputException;
import com.example.onlinechatroomdemoproject.model.User;
import com.example.onlinechatroomdemoproject.service.MailService;
import com.example.onlinechatroomdemoproject.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@Controller
@SessionAttributes({"user", "codeSentAt"})
@RequestMapping("/signup")
public class SignUpController {
    UserService userService;
    MailService mailService;

    public SignUpController(UserService userService, MailService mailService){
        this.userService = userService;
        this.mailService = mailService;
    }

    @GetMapping
    public String getSignUpPage(@RequestParam(required = false) String error, Model model){
        if(error != null){
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        return "index";
    }

    @GetMapping("/resend-code")
    public String resendCode(@SessionAttribute("user") User user) throws MessagingException {
        String confirmCode = mailService.mailConfirmationCode(user.getEmail(), "Email confirmation");
        return "redirect:/email?code=" + confirmCode;
    }

    @GetMapping("/generate-username")
    public ResponseEntity<String> generateUsernameSuggestion(@RequestParam String name,
                                                             @RequestParam String lastname){

        String generatedUsername = userService.generateUsername(name, lastname);
        return ResponseEntity.ok(generatedUsername);
    }


    @PostMapping
    public String signUp(
            @RequestParam String email,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String repeatPassword,
            @RequestParam String phoneNumber,
            Model model
    ) throws MessagingException {
        User user = null;
        try{
            user = userService.signUp(email, username, password, repeatPassword, phoneNumber);
            model.addAttribute("user", user);

        }catch (InvalidInputException ex) {
            return "redirect:/signup?error=" + ex.getMessage();
        }

        String confirmCode = mailService.mailConfirmationCode(email, "Email confirmation");
        LocalDateTime codeSentAt = LocalDateTime.now();
        model.addAttribute("codeSentAt", codeSentAt);
        return "redirect:/email?code=" + confirmCode;
    }
}
