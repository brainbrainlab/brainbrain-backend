package site.brainbrain.iqtest.service.payment.dto;

public record ApiConfirmRequest(
        String paymentKey,
        long amount,
        long orderId
) {
}
