package site.brainbrain.iqtest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.brainbrain.iqtest.domain.payment.NicePayment;
import site.brainbrain.iqtest.exception.PaymentException;

@Repository
public interface NicePaymentRepository extends JpaRepository<NicePayment, Long> {

    Optional<NicePayment> findByOrderId(final String orderId);

    default NicePayment fetchByOrderId(final String orderId) {
        return findByOrderId(orderId).orElseThrow(() -> new PaymentException("주문번호와 일치하는 결제 내역이 없습니다."));
    }
}
