package site.brainbrain.iqtest.infrastructure.payment.nice.dto;

import java.util.Map;

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
        return new NicePaymentCallbackRequest(
                params.get("authResultCode"),
                params.get("authResultMsg"),
                params.get("tid"),
                params.get("clientId"),
                params.get("orderId"),
                Integer.parseInt(params.get("amount")),
                params.get("mallReserved"),
                params.get("authToken"),
                params.get("signature")
        );
    }
}
