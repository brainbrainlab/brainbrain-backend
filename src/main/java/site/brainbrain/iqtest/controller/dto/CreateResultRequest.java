package site.brainbrain.iqtest.controller.dto;

public record CreateResultRequest(
        String email, String name, int age, String gender, String country,
        String paymentHistory, String answerSheet
) {
}
