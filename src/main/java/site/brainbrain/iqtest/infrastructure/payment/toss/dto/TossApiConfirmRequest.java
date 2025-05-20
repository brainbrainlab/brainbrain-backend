package site.brainbrain.iqtest.infrastructure.payment.toss.dto;

public record TossApiConfirmRequest (
        String paymentKey,
        int amount,
        String orderId
) {
}
