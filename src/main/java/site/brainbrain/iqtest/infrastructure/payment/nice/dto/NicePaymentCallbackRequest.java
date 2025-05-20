package site.brainbrain.iqtest.infrastructure.payment.nice.dto;

public record NicePaymentCallbackRequest(String authResultCode,
                                         String authResultMsg,
                                         String tid,
                                         String clientId,
                                         String orderId,
                                         Integer amount,
                                         String mallReserved,
                                         String authToken,
                                         String signature) {
}
