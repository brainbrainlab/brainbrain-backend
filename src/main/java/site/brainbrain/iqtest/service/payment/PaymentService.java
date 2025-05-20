package site.brainbrain.iqtest.service.payment;

public interface PaymentService<T> {

    void pay(final T request);
}
