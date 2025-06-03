package site.brainbrain.iqtest.infrastructure.payment.nice.dto;

public record NiceApproveRequest(Integer amount, String ediDate, String signData) {
}
