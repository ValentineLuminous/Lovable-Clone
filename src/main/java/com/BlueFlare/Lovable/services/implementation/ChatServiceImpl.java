package com.BlueFlare.Lovable.services.implementation;

import com.BlueFlare.Lovable.dto.chat.ChatResponse;
import com.BlueFlare.Lovable.entity.ChatMessage;
import com.BlueFlare.Lovable.entity.ChatSession;
import com.BlueFlare.Lovable.entity.ChatSessionId;
import com.BlueFlare.Lovable.mapper.ChatMapper;
import com.BlueFlare.Lovable.repository.ChatMessageRepository;
import com.BlueFlare.Lovable.repository.ChatSessionRepository;
import com.BlueFlare.Lovable.security.AuthUtil;
import com.BlueFlare.Lovable.services.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final AuthUtil authUtil;
    private final ChatMapper chatMapper;

    @Override
    public List<ChatResponse> getProjectChatHistory(Long projectId) {
        Long userId = authUtil.getCurrentUserId();

        ChatSession chatSession = chatSessionRepository.getReferenceById(
                new ChatSessionId(projectId, userId)
        );

        List<ChatMessage> chatMessageList = chatMessageRepository.findByChatSession(chatSession);

        return chatMapper.fromListOfChatMessage(chatMessageList);
    }
}

