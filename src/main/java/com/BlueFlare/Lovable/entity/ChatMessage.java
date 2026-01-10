package com.BlueFlare.Lovable.entity;

import com.BlueFlare.Lovable.enums.MessageRole;

import java.time.Instant;

public class ChatMessage {
    Long id;
    ChatSession chatSession;
    String Content;
    MessageRole role;
    String toolCalls;
    Integer tokensUsed;
    Instant createdAt;

}
