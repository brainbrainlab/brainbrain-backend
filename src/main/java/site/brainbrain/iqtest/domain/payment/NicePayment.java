package site.brainbrain.iqtest.domain.payment;

import java.time.OffsetDateTime;

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
import site.brainbrain.iqtest.domain.PurchaseOption;
import site.brainbrain.iqtest.infrastructure.payment.nice.dto.NiceApiConfirmResponse;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class NicePayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String tid;

    @Column(unique = true)
    private String orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PurchaseOption purchaseOption;

    private String resultCode;
    private String resultMsg;
    private String status;
    private OffsetDateTime paidAt;
    private String payMethod;
    private Integer amount;
    private String currency;
    private String clientId;

    @Builder
    private NicePayment(final String tid,
                        final String orderId,
                        final PurchaseOption purchaseOption,
                        final String resultCode,
                        final String resultMsg,
                        final String status,
                        final OffsetDateTime paidAt,
                        final String payMethod,
                        final Integer amount,
                        final String currency,
                        final String clientId) {
        this.tid = tid;
        this.orderId = orderId;
        this.purchaseOption = purchaseOption;
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
        this.status = status;
        this.paidAt = paidAt;
        this.payMethod = payMethod;
        this.amount = amount;
        this.currency = currency;
        this.clientId = clientId;
    }

    public static NicePayment of(final NiceApiConfirmResponse confirmResponse, final String clientId) {
        return NicePayment.builder()
                .tid(confirmResponse.tid())
                .orderId(confirmResponse.orderId())
                .purchaseOption(PurchaseOption.from(confirmResponse.goodsName()))
                .resultCode(confirmResponse.resultCode())
                .resultMsg(confirmResponse.resultMsg())
                .status(confirmResponse.status())
                .paidAt(confirmResponse.paidAt())
                .payMethod(confirmResponse.payMethod())
                .amount(confirmResponse.amount())
                .currency(confirmResponse.currency())
                .clientId(clientId)
                .build();
    }
}
