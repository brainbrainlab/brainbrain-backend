package site.brainbrain.iqtest.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CouponType type;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private double discountRate;

    @Column(nullable = false)
    private boolean isAvailable;

    @Column(nullable = true)
    private LocalDateTime expiredAt;

    @Column(nullable = true)
    private LocalDateTime usedAt;

    @Builder
    public Coupon(final CouponType type,
                  final String code,
                  final double discountRate,
                  final boolean isAvailable,
                  final LocalDateTime expiredAt,
                  final LocalDateTime usedAt) {
        this.type = type;
        this.code = code;
        this.discountRate = discountRate;
        this.isAvailable = isAvailable;
        this.expiredAt = expiredAt;
        this.usedAt = usedAt;
    }
}
