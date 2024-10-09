package com.example.onlinechatroomdemoproject.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Message {
    private String from;
    private String to;
    private String body;
    private LocalDateTime timeSent;
    private MessageStatus messageStatus;

    public Message(String from, String to, String body, LocalDateTime timeSent, MessageStatus messageStatus) {
        this.from = from;
        this.to = to;
        this.body = body;
        this.timeSent = timeSent;
        this.messageStatus = messageStatus;
    }
}
