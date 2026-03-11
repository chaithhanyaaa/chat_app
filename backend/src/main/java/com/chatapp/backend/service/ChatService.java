package com.chatapp.backend.service;

import com.chatapp.backend.dto.ConversationDTO;
import com.chatapp.backend.dto.MessageResponseDTO;
import com.chatapp.backend.entity.Conversation;
import com.chatapp.backend.entity.Message;
import com.chatapp.backend.entity.User;
import com.chatapp.backend.repository.ConversationRepository;
import com.chatapp.backend.repository.MessageRepository;
import com.chatapp.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ChatService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public ChatService(ConversationRepository conversationRepository,
                       MessageRepository messageRepository,
                       UserRepository userRepository) {

        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public List<ConversationDTO> getUserConversations(Long userId) {

        List<Conversation> conversations =
                conversationRepository.findByUser1IdOrUser2Id(userId, userId);

        List<ConversationDTO> result = new ArrayList<>();

        for (Conversation conversation : conversations) {

            Long otherUserId;

            if (conversation.getUser1Id().equals(userId)) {
                otherUserId = conversation.getUser2Id();
            } else {
                otherUserId = conversation.getUser1Id();
            }

            User otherUser = userRepository.findById(otherUserId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<Message> messages =
                    messageRepository.findByConversationIdOrderByCreatedAtAsc(conversation.getId());

            String lastMessage = null;
            LocalDateTime time = null;

            if (!messages.isEmpty()) {
                Message last = messages.get(messages.size() - 1);
                lastMessage = last.getContent();
                time = last.getCreatedAt();
            }

            ConversationDTO dto = new ConversationDTO();

            dto.setConversationId(conversation.getId());
            dto.setUserId(otherUserId);
            dto.setUsername(otherUser.getUsername());
            dto.setLastMessage(lastMessage);
            dto.setTime(time);

            result.add(dto);
        }

        return result;
    }



    public List<MessageResponseDTO> getMessages(Long conversationId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Message> messagePage =
                messageRepository.findByConversationId(conversationId, pageable);

        List<Message> messages = messagePage.getContent();

        Collections.reverse(messages); // oldest → newest for UI

        List<MessageResponseDTO> response = new ArrayList<>();

        for (Message message : messages) {
            response.add(new MessageResponseDTO(
                    message.getId(),
                    message.getSenderId(),
                    message.getContent(),
                    message.getStatus(),
                    message.getCreatedAt()
            ));
        }

        return response;
    };

}