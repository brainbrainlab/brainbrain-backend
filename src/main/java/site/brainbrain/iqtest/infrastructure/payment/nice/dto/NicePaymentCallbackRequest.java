package site.brainbrain.iqtest.infrastructure.payment.nice.dto;

import java.util.Map;

import lombok.Builder;

@Builder
public record NicePaymentCallbackRequest(String authResultCode,
                                         String authResultMsg,
                                         String tid,
                                         String clientId,
                                         String orderId,
                                         Integer amount,
                                         String mallReserved,
                                         String authToken,
                                         String signature) {

    public static NicePaymentCallbackRequest from(final Map<String, String> params) {
        return NicePaymentCallbackRequest.builder()
                .authResultCode(params.get("authResultCode"))
                .authResultMsg(params.get("authResultMsg"))
                .tid(params.get("tid"))
                .clientId(params.get("clientId"))
                .orderId(params.get("orderId"))
                .amount(Integer.parseInt(params.get("amount")))
                .mallReserved(params.get("mallReserved"))
                .authToken(params.get("authToken"))
                .signature(params.get("signature"))
                .build();
    }
}
