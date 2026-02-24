package com.BlueFlare.Lovable.mapper;

import com.BlueFlare.Lovable.dto.chat.ChatResponse;
import com.BlueFlare.Lovable.entity.ChatMessage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    List<ChatResponse> fromListOfChatMessage(List<ChatMessage> chatMessageList);
}
