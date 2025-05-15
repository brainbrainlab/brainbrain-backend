package site.brainbrain.iqtest.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.OffsetDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", unique = true)
    private String orderId;

    @Column(name = "order_name")
    private String orderName;

    @Column(name = "amount")
    private int amount;

    @Column(name = "payment_key", unique = true)
    private String paymentKey;

    @Column(name = "requested_at")
    private OffsetDateTime requestedAt;

    @Column(name = "is_canceled")
    private boolean isCanceled;

    @Builder
    private Payment(final String orderId, final String orderName, final int amount, final String paymentKey,
                   final OffsetDateTime requestedAt, final boolean isCanceled) {
        this.orderId = orderId;
        this.orderName = orderName;
        this.amount = amount;
        this.paymentKey = paymentKey;
        this.requestedAt = requestedAt;
        this.isCanceled = isCanceled;
    }
}
