package site.brainbrain.iqtest.service.payment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentApiUri {

    CONFIRM("/v1/payments/confirm");

    private final String uri;
}
