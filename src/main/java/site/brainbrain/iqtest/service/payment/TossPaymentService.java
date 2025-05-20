package site.brainbrain.iqtest.service.payment;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.brainbrain.iqtest.domain.payment.TossPayment;
import site.brainbrain.iqtest.infrastructure.payment.toss.TossPaymentClient;
import site.brainbrain.iqtest.infrastructure.payment.toss.dto.TossApiConfirmRequest;
import site.brainbrain.iqtest.infrastructure.payment.toss.dto.TossApiConfirmResponse;
import site.brainbrain.iqtest.repository.TossPaymentRepository;

@RequiredArgsConstructor
@Service
public class TossPaymentService implements PaymentService<TossApiConfirmRequest> {

    private final TossPaymentClient tossPaymentClient;
    private final TossPaymentRepository tossPaymentRepository;

    @Transactional
    @Override
    public void pay(final TossApiConfirmRequest request) {
        final TossApiConfirmResponse confirm = tossPaymentClient.confirm(request);
        final TossPayment payment = TossPayment.of(request.paymentKey(), confirm);
        tossPaymentRepository.save(payment);
    }
}
