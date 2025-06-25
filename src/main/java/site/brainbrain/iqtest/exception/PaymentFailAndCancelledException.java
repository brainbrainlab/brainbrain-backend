package site.brainbrain.iqtest.exception;

public class PaymentFailAndCancelledException extends RuntimeException {

    public PaymentFailAndCancelledException(String message) {
        super(message);
    }
}
