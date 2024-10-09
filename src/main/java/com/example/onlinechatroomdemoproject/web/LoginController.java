package com.example.onlinechatroomdemoproject.web;

import com.example.onlinechatroomdemoproject.exceptions.InvalidInputException;
import com.example.onlinechatroomdemoproject.model.User;
import com.example.onlinechatroomdemoproject.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes({"user", "username"})
@RequestMapping("/login")
public class LoginController {
    UserService userService;

    public LoginController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public String getLoginPage(){
        return "login";
    }

    @PostMapping
    public String login(@RequestParam String usernameOrEmail,
                        @RequestParam String password,
                        Model model){
        try {
            User user = userService.login(usernameOrEmail, password);
            model.addAttribute("user", user);
            model.addAttribute("username", user.getUsername());

        }catch (InvalidInputException ex){
            return "redirect:/login?error=" + ex.getMessage();
        }

        return "chat";
    }
}
