package site.brainbrain.iqtest.service.payment;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.brainbrain.iqtest.controller.dto.PaymentConfirmResponse;
import site.brainbrain.iqtest.domain.PurchaseOption;
import site.brainbrain.iqtest.domain.payment.NicePayment;
import site.brainbrain.iqtest.exception.PaymentClientException;
import site.brainbrain.iqtest.infrastructure.payment.nice.NicePaymentClient;
import site.brainbrain.iqtest.infrastructure.payment.nice.dto.NiceApiConfirmResponse;
import site.brainbrain.iqtest.infrastructure.payment.nice.dto.NicePaymentCallbackRequest;
import site.brainbrain.iqtest.repository.NicePaymentRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class NicePaymentService implements PaymentService {

    private static final String SUCCESS_RESULT_CODE = "0000";

    private final NicePaymentClient nicePaymentClient;
    private final NicePaymentRepository nicePaymentRepository;

    @Transactional
    public PaymentConfirmResponse pay(final Map<String, String> params) {
        final NicePaymentCallbackRequest request = NicePaymentCallbackRequest.from(params);

        validateNiceResultCode(request.authResultCode());
        final NiceApiConfirmResponse confirmResponse = nicePaymentClient.confirm(request);
        validateNiceResultCode(confirmResponse.resultCode());

        final NicePayment payment = NicePayment.from(confirmResponse);
        nicePaymentRepository.save(payment);
        return new PaymentConfirmResponse(payment.getOrderId());
    }

    private void validateNiceResultCode(final String resultCode) {
        if (!resultCode.equals(SUCCESS_RESULT_CODE)) {
            throw new PaymentClientException("결제 인증에 실패했습니다. result code : " + resultCode);
        }
    }

    @Transactional(readOnly = true)
    public PurchaseOption getPurchaseOptionByOrderId(final String orderId) {
        final NicePayment nicePayment = nicePaymentRepository.fetchByOrderId(orderId);
        return nicePayment.getPurchaseOption();
    }
}
