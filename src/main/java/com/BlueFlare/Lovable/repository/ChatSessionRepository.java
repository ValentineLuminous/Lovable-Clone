package com.BlueFlare.Lovable.repository;

import com.BlueFlare.Lovable.entity.ChatSession;
import com.BlueFlare.Lovable.entity.ChatSessionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatSessionRepository extends JpaRepository<ChatSession, ChatSessionId> {
}
