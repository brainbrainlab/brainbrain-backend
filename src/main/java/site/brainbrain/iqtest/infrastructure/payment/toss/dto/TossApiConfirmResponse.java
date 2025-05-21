package site.brainbrain.iqtest.infrastructure.payment.toss.dto;

import java.time.OffsetDateTime;

public record TossApiConfirmResponse(
        String orderId,
        String orderName,
        int amount,
        String paymentKey,
        OffsetDateTime requestedAt
) {
}
