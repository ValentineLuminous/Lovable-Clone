package com.BlueFlare.Lovable.dto.subscription;

public record UsageTodayResponse(
        Integer tokenUsed,
        Integer tokenLimit,
        Integer previewRunning,
        Integer previewLimit
) {
}
