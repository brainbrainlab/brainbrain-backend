package site.brainbrain.iqtest.service.payment;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.brainbrain.iqtest.controller.dto.CreateResultRequest;
import site.brainbrain.iqtest.domain.Payment;
import site.brainbrain.iqtest.repository.PaymentRepository;
import site.brainbrain.iqtest.service.payment.dto.ApiConfirmRequest;
import site.brainbrain.iqtest.service.payment.dto.ApiConfirmResponse;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentClient paymentClient;
    private final PaymentRepository paymentRepository;

    @Transactional
    public String pay(final CreateResultRequest request) {
        final ApiConfirmResponse confirm = paymentClient.confirm(
                new ApiConfirmRequest(request.paymentKey(), request.amount(), request.orderId())
        );
        final Payment payment = createPayment(request.paymentKey(), confirm);
        paymentRepository.save(payment);
        return payment.getOrderId();
    }

    private Payment createPayment(final String paymentKey, final ApiConfirmResponse confirm) {
        return Payment.builder()
                .orderId(confirm.orderId())
                .orderName(confirm.orderName())
                .amount(confirm.amount())
                .paymentKey(paymentKey)
                .requestedAt(confirm.requestedAt())
                .isCanceled(false)
                .build();
    }
}
