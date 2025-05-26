package site.brainbrain.iqtest.infrastructure.payment.toss.dto;

import java.util.Map;

public record TossApiConfirmRequest(String paymentKey,
                                    int amount,
                                    String orderId) {

    public static TossApiConfirmRequest from(final Map<String, String> params) {
        return new TossApiConfirmRequest(
                params.get("paymentKey"),
                Integer.parseInt(params.get("amount")),
                params.get("orderId"));
    }
}
