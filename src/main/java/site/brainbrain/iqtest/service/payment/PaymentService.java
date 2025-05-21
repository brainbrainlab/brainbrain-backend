package site.brainbrain.iqtest.service.payment;

/**
 * @param <T> PG사별 결제 요청 DTO 타입 (토스: TossApiConfirmRequest, 나이스: NicePaymentCallbackRequest)
 */
public interface PaymentService<T> {

    void pay(final T request);
}
