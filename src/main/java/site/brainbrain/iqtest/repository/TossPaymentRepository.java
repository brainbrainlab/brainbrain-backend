package site.brainbrain.iqtest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.brainbrain.iqtest.domain.payment.TossPayment;
import site.brainbrain.iqtest.exception.PaymentException;

@Repository
public interface TossPaymentRepository extends JpaRepository<TossPayment, Long> {

    Optional<TossPayment> findByOrderId(final String orderId);

    default TossPayment fetchByOrderId(final String orderId) {
        return findByOrderId(orderId).orElseThrow(() -> new PaymentException("주문번호와 일치하는 토스 결제 내역이 없습니다."));
    }
}
