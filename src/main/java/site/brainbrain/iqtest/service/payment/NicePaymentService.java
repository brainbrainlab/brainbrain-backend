package site.brainbrain.iqtest.service.payment;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.brainbrain.iqtest.domain.payment.NicePayment;
import site.brainbrain.iqtest.exception.PaymentClientException;
import site.brainbrain.iqtest.infrastructure.payment.nice.NicePaymentClient;
import site.brainbrain.iqtest.infrastructure.payment.nice.dto.NiceApiConfirmResponse;
import site.brainbrain.iqtest.infrastructure.payment.nice.dto.NicePaymentCallbackRequest;
import site.brainbrain.iqtest.repository.NicePaymentRepository;

@RequiredArgsConstructor
@Service
public class NicePaymentService implements PaymentService<NicePaymentCallbackRequest> {

    private static final String SUCCESS_RESULT_CODE = "0000";

    private final NicePaymentClient nicePaymentClient;
    private final NicePaymentRepository nicePaymentRepository;

    @Transactional
    @Override
    public void pay(final NicePaymentCallbackRequest request) {
        validateNiceResultCode(request.authResultCode());
        final NiceApiConfirmResponse confirmResponse = nicePaymentClient.confirm(request);
        validateNiceResultCode(confirmResponse.resultCode());
        final NicePayment Payment = NicePayment.from(confirmResponse);
        nicePaymentRepository.save(Payment);
    }

    private void validateNiceResultCode(final String resultCode) {
        if (!resultCode.equals(SUCCESS_RESULT_CODE)) {
            throw new PaymentClientException("결제 인증에 실패했습니다.");
        }
    }
}
