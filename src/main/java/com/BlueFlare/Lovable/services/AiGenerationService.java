package com.BlueFlare.Lovable.services;

import com.BlueFlare.Lovable.dto.chat.StreamResponse;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

public interface AiGenerationService {
    Flux<StreamResponse> streamResponse(String message, Long projectId);
}
