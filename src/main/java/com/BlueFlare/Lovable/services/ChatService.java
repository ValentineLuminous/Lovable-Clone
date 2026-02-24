package com.BlueFlare.Lovable.services;


import com.BlueFlare.Lovable.dto.chat.ChatResponse;

import java.util.List;

public interface ChatService {
    List<ChatResponse> getProjectChatHistory(Long projectId);
}
