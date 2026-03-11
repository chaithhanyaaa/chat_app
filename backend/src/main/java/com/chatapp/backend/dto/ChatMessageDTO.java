package com.chatapp.backend.dto;

public class ChatMessageDTO {

    private Long conversationId;
    private Long senderId;
    private String content;

    public ChatMessageDTO() {
    }

    public ChatMessageDTO(Long conversationId, Long senderId, String content) {
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.content = content;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public void setContent(String content) {
        this.content = content;
    }
}