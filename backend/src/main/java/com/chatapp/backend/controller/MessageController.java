package com.chatapp.backend.controller;

import com.chatapp.backend.dto.MessageResponseDTO;
import com.chatapp.backend.service.ChatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final ChatService chatService;

    public MessageController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/{conversationId}")
    public List<MessageResponseDTO> getMessages(
            @PathVariable Long conversationId,
            @RequestParam int page,
            @RequestParam int size) {

        return chatService.getMessages(conversationId, page, size);
    }
}