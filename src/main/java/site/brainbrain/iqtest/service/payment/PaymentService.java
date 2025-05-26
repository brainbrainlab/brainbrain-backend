package site.brainbrain.iqtest.service.payment;

import java.util.Map;

import site.brainbrain.iqtest.controller.dto.PaymentConfirmResponse;
import site.brainbrain.iqtest.domain.PurchaseOption;

public interface PaymentService {

    PaymentConfirmResponse pay(final Map<String, String> params);

    PurchaseOption getPurchaseOptionByOrderId(final String orderId);
}
