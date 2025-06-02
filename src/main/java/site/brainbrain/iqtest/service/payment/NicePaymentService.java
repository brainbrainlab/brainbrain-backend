package site.brainbrain.iqtest.service.payment;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import site.brainbrain.iqtest.controller.dto.PaymentConfirmResponse;
import site.brainbrain.iqtest.domain.PurchaseOption;
import site.brainbrain.iqtest.domain.payment.NicePayment;
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
        final NicePayment payment = NicePayment.of(confirmResponse, request.clientId());
        nicePaymentRepository.save(payment);
        return new PaymentConfirmResponse(payment.getOrderId());
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
