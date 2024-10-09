package com.example.onlinechatroomdemoproject.web;


import com.example.onlinechatroomdemoproject.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.HashSet;
import java.util.Set;

@Controller
@SessionAttributes({"user", "username"})
@RequestMapping("/chat")
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private static final Set<String> activeUsers = new HashSet<>();

    public ChatController(SimpMessagingTemplate messagingTemplate){
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/username")
    @ResponseBody
    public String getUsername(Model model){
        String username = (String) model.getAttribute("username");
        System.out.println(username);
        return username != null? username : "";
    }

    @MessageMapping("/public-message") // /app/public-message
    @SendTo("/chatroom/public")
    public Message publicMessage(@Payload Message message){
        return message;
    }

    @MessageMapping("/private-message") // /app/private-message
    public Message privateMessage(@Payload Message message){

        //will be sent to /user/private  (/user is our configured topic prefix for an user destination)
        messagingTemplate.convertAndSendToUser(message.getFrom(), "/private", message); // user is listening to topic ex. /user/ana123/private (ana123 is dynamic)
        return message;
    }

    @MessageMapping("/user-join") // /app/user-join
    public void userJoin(@Payload String username){
        System.out.println("User joined " + username);

        synchronized (this) {
            activeUsers.add(username);
            notifyActiveUsers();
        }
    }

    @MessageMapping("/user-leave")
    public void userLeave(@Payload String username){
        System.out.println("User left " + username);

        synchronized (this) {
            activeUsers.remove(username);
            notifyActiveUsers();
        }
    }

    public void notifyActiveUsers(){
        messagingTemplate.convertAndSend("/topic/active-users", activeUsers);
    }
}
