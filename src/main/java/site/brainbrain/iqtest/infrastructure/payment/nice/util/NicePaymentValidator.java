package site.brainbrain.iqtest.infrastructure.payment.nice.util;

import site.brainbrain.iqtest.exception.PaymentClientException;
import site.brainbrain.iqtest.exception.PaymentException;

public class NicePaymentValidator {

    private static final String SUCCESS_RESULT_CODE = "0000";

    public static void validateNiceResultCode(final String resultCode) {
        if (!resultCode.equals(SUCCESS_RESULT_CODE)) {
            throw new PaymentClientException("결제 인증에 실패했습니다. result code : " + resultCode);
        }
    }

    public static void validateSignature(final String signature, final String expectedSignature) {
        if (!signature.equals(expectedSignature)) {
            throw new PaymentException("위변조 검증 데이터가 일치하지 않습니다.");
        }
    }
}
