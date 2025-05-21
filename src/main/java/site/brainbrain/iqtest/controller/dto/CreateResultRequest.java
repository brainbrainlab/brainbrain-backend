package site.brainbrain.iqtest.controller.dto;

import site.brainbrain.iqtest.infrastructure.payment.nice.dto.NicePaymentCallbackRequest;

public record CreateResultRequest(
        String email, String name, int age, String gender, String country,
        String paymentHistory, String answerSheet,
        NicePaymentCallbackRequest nicePaymentCallbackRequest
) {
}
