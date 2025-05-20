package site.brainbrain.iqtest.infrastructure.payment.nice.dto;

import java.time.OffsetDateTime;

public record NiceApiCancelResponse(String resultCode,
                                    String resultMsg,
                                    String tid,
                                    String orderId,
                                    String status,
                                    OffsetDateTime failedAt,
                                    OffsetDateTime cancelledAt,
                                    String payMethod,
                                    Integer amount,
                                    String goodsName,
                                    String currency) {
}
