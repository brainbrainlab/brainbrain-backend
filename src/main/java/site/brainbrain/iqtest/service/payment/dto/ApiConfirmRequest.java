package site.brainbrain.iqtest.service.payment.dto;

public record ApiConfirmRequest(
        String paymentKey,
        int amount,
        String orderId
) {
}
