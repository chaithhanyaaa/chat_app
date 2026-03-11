package com.chatapp.backend.dto;

import java.time.LocalDateTime;

public class MessageResponseDTO {

    private Long messageId;
    private Long senderId;
    private String content;
    private String status;
    private LocalDateTime createdAt;

    public MessageResponseDTO(Long messageId, Long senderId, String content, String status, LocalDateTime createdAt) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.content = content;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getMessageId() {
        return messageId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}