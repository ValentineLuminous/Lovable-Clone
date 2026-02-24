package com.BlueFlare.Lovable.repository;

import com.BlueFlare.Lovable.entity.ChatEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatEventRepository extends JpaRepository<ChatEvent, Long> {
}
