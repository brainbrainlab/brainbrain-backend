package site.brainbrain.iqtest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.brainbrain.iqtest.domain.payment.NicePayment;

@Repository
public interface NicePaymentRepository extends JpaRepository<NicePayment, Long> {
}
