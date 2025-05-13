package site.brainbrain.iqtest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import site.brainbrain.iqtest.domain.Coupon;
import site.brainbrain.iqtest.exception.CouponException;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findByCode(final String couponCode);

    default Coupon fetchByCode(final String couponCode) {
        return findByCode(couponCode).orElseThrow(() -> new CouponException("쿠폰을 찾을 수 없습니다."));
    }
}
