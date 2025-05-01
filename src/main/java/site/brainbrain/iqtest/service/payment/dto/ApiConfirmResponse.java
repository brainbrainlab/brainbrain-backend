package site.brainbrain.iqtest.service.payment.dto;

import java.time.OffsetDateTime;

public record ApiConfirmResponse(
        String orderId,
        String orderName,
        String amount,
        String paymentKey,
        OffsetDateTime requestedAt
) {
}
