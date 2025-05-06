package site.brainbrain.iqtest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.brainbrain.iqtest.domain.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
