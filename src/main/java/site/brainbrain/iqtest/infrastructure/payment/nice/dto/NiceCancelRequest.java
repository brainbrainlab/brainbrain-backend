package site.brainbrain.iqtest.infrastructure.payment.nice.dto;

import lombok.Builder;
import site.brainbrain.iqtest.domain.payment.NicePayment;

@Builder
public record NiceCancelRequest(String tid,
                                String clientId,
                                String orderId) {

    public static NiceCancelRequest from(final NicePaymentCallbackRequest callbackRequest) {
        return NiceCancelRequest.builder()
                .tid(callbackRequest.tid())
                .clientId(callbackRequest.clientId())
                .orderId(callbackRequest.clientId())
                .build();
    }

    public static NiceCancelRequest from(final NicePayment nicePayment) {
        return NiceCancelRequest.builder()
                .tid(nicePayment.getTid())
                .clientId(nicePayment.getClientId())
                .orderId(nicePayment.getOrderId())
                .build();
    }
}
