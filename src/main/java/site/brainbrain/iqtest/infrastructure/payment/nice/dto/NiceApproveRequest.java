package site.brainbrain.iqtest.infrastructure.payment.nice.dto;

public record NiceApproveRequest(Integer amount) {
}
// todo: amount만 필수값. 클라이언트 연동 테스트 후 String ediDate,String signData,String returnCharSet 추가 예정
