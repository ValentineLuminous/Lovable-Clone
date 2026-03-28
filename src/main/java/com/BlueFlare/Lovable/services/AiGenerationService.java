package com.BlueFlare.Lovable.services;

import com.BlueFlare.Lovable.dto.chat.StreamResponse;
import reactor.core.publisher.Flux;

public interface AiGenerationService {
    Flux<StreamResponse> streamResponse(String message, Long projectId);
}
