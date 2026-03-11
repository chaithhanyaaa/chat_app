package com.chatapp.backend.controller;

import com.chatapp.backend.dto.ChatMessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessageDTO message) {

        System.out.println("Message received: " + message.getContent());

    }
}