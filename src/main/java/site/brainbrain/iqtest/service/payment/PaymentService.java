package site.brainbrain.iqtest.service.payment;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.brainbrain.iqtest.controller.dto.CreateResultRequest;
import site.brainbrain.iqtest.domain.Payment;
import site.brainbrain.iqtest.repository.PaymentRepository;
import site.brainbrain.iqtest.service.payment.dto.ApiConfirmRequest;
import site.brainbrain.iqtest.service.payment.dto.ApiConfirmResponse;

@RequiredArgsConstructor
@Component
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
        return new Payment(
                confirm.orderId(),
                confirm.orderName(),
                confirm.amount(),
                paymentKey,
                confirm.requestedAt(),
                false
        );
    }

    // todo: 환불 - 주문번호를 고객이 알고있어야 환불을 해줄 수 있음.
    @Transactional
    public void refund() {

    }

    // todo: 결제 내역 조회
    public void view() {

    }
}
