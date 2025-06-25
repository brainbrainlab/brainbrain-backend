package site.brainbrain.iqtest.service.payment;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import site.brainbrain.iqtest.controller.dto.PaymentConfirmResponse;
import site.brainbrain.iqtest.domain.PurchaseOption;
import site.brainbrain.iqtest.domain.payment.NicePayment;
import site.brainbrain.iqtest.exception.PaymentFailAndCancelledException;
import site.brainbrain.iqtest.exception.PaymentServerException;
import site.brainbrain.iqtest.infrastructure.payment.nice.NicePaymentClient;
import site.brainbrain.iqtest.infrastructure.payment.nice.dto.NiceApiConfirmResponse;
import site.brainbrain.iqtest.infrastructure.payment.nice.dto.NiceCancelRequest;
import site.brainbrain.iqtest.infrastructure.payment.nice.dto.NicePaymentCallbackRequest;
import site.brainbrain.iqtest.infrastructure.payment.nice.util.NicePaymentValidator;
import site.brainbrain.iqtest.infrastructure.payment.nice.util.SignatureGenerator;
import site.brainbrain.iqtest.repository.NicePaymentRepository;

@Slf4j
@Service
public class NicePaymentService implements PaymentService {

    private final String apiSecretKey;
    private final NicePaymentClient nicePaymentClient;
    private final NicePaymentRepository nicePaymentRepository;

    public NicePaymentService(@Value("${payment.nice.secret}") final String apiSecretKey,
                              final NicePaymentClient nicePaymentClient,
                              final NicePaymentRepository nicePaymentRepository) {
        this.apiSecretKey = apiSecretKey;
        this.nicePaymentClient = nicePaymentClient;
        this.nicePaymentRepository = nicePaymentRepository;
    }

    @Transactional
    @Override
    public PaymentConfirmResponse pay(final Map<String, String> params) {
        final NicePaymentCallbackRequest request = NicePaymentCallbackRequest.from(params);
        validateCallbackRequest(request);

        final NiceApiConfirmResponse confirmResponse = nicePaymentClient.confirm(request);
        try {
            final NicePayment payment = NicePayment.of(confirmResponse, request.clientId());
            nicePaymentRepository.save(payment);
            return new PaymentConfirmResponse(payment.getOrderId());
        } catch (final Exception e) {
            final NiceApiConfirmResponse response = nicePaymentClient.checkPaymentStatus(confirmResponse.tid(), request.clientId());
            if (Objects.equals(response.status(), "paid")) {
                final NiceCancelRequest niceCancelRequest = NiceCancelRequest.from(request);
                nicePaymentClient.cancel(niceCancelRequest);
                log.error("나이스 결제 승인 이후 로직에서 예외 발생 - 결제 취소 완료: {}", e.getMessage());
                throw new PaymentFailAndCancelledException("결제에 실패하여 승인 취소되었습니다.");
            }
            log.error("나이스 결제 승인 이후 로직에서 예외 발생 : {}", e.getMessage());
            throw new PaymentServerException("결제 승인에 실패했습니다.");
        }
    }

    private void validateCallbackRequest(final NicePaymentCallbackRequest request) {
        NicePaymentValidator.validateNiceResultCode(request.authResultCode());
        final String expectedSignature = SignatureGenerator.generate(request.authToken(),
                request.clientId(),
                request.amount(),
                apiSecretKey);
        NicePaymentValidator.validateSignature(request.signature(), expectedSignature);
    }

    @Transactional(readOnly = true)
    @Override
    public PurchaseOption getPurchaseOptionByOrderId(final String orderId) {
        final NicePayment nicePayment = nicePaymentRepository.fetchByOrderId(orderId);
        return nicePayment.getPurchaseOption();
    }

    @Transactional
    @Override
    public void cancel(final String orderId) {
        final NicePayment nicePayment = nicePaymentRepository.fetchByOrderId(orderId);
        final NiceCancelRequest niceCancelRequest = NiceCancelRequest.from(nicePayment);
        nicePaymentClient.cancel(niceCancelRequest);
    }
}
