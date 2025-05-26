package site.brainbrain.iqtest.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import site.brainbrain.iqtest.domain.Coupon;
import site.brainbrain.iqtest.exception.CouponException;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findByCode(final String couponCode);

    default Coupon fetchByCode(final String couponCode) {
        return findByCode(couponCode).orElseThrow(() -> new CouponException("쿠폰을 찾을 수 없습니다."));
    }

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Coupon "
            + "SET isAvailable = false, usedAt = :usedAt "
            + "WHERE code = :code AND isAvailable = true")
    int markAsUsed(@Param("code") final String code, @Param("usedAt") final LocalDateTime usedAt);
}
