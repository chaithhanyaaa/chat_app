package com.chatapp.backend.controller;

import com.chatapp.backend.dto.ConversationDTO;
import com.chatapp.backend.service.ChatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conversations")
public class ConversationController {

    private final ChatService chatService;

    public ConversationController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/{userId}")
    public List<ConversationDTO> getUserConversations(@PathVariable Long userId) {
        return chatService.getUserConversations(userId);
    }

    @GetMapping
    public String test() {
        return "test";
    }
}