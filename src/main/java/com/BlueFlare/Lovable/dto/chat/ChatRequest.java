package com.BlueFlare.Lovable.dto.chat;

public record ChatRequest(
        String message,
        Long projectId
) {
}
