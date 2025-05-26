package site.brainbrain.iqtest.service.payment;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.brainbrain.iqtest.controller.dto.PaymentConfirmResponse;
import site.brainbrain.iqtest.domain.PurchaseOption;
import site.brainbrain.iqtest.domain.payment.TossPayment;
import site.brainbrain.iqtest.infrastructure.payment.toss.TossPaymentClient;
import site.brainbrain.iqtest.infrastructure.payment.toss.dto.TossApiConfirmRequest;
import site.brainbrain.iqtest.infrastructure.payment.toss.dto.TossApiConfirmResponse;
import site.brainbrain.iqtest.repository.TossPaymentRepository;

@RequiredArgsConstructor
@Service
public class TossPaymentService implements PaymentService {

    private final TossPaymentClient tossPaymentClient;
    private final TossPaymentRepository tossPaymentRepository;

    @Transactional
    @Override
    public PaymentConfirmResponse pay(final Map<String, String> params) {
        final TossApiConfirmRequest request = TossApiConfirmRequest.from(params);
        final TossApiConfirmResponse confirm = tossPaymentClient.confirm(request);
        final PurchaseOption purchaseOption = PurchaseOption.from(params.get("purchaseOption"));
        final TossPayment payment = TossPayment.of(request.paymentKey(), confirm, purchaseOption);
        tossPaymentRepository.save(payment);
        return new PaymentConfirmResponse(payment.getOrderId());
    }

    @Transactional(readOnly = true)
    @Override
    public PurchaseOption getPurchaseOptionByOrderId(final String orderId) {
        final TossPayment tossPayment = tossPaymentRepository.fetchByOrderId(orderId);
        return tossPayment.getPurchaseOption();
    }

    @Transactional
    @Override
    public void cancel(final String orderId) {
        //todo 토스 pg 결제 취소 구현
    }
}
