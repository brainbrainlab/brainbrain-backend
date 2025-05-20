package site.brainbrain.iqtest.infrastructure.payment.nice.dto;

import java.time.OffsetDateTime;

public record NiceApiConfirmResponse(String resultCode,
                                     String resultMsg,
                                     String tid,
                                     String orderId,
                                     String status,
                                     OffsetDateTime paidAt,
                                     String payMethod,
                                     Integer amount,
                                     String goodsName,
                                     String currency) {
}
