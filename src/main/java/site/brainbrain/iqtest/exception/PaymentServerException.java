package site.brainbrain.iqtest.exception;

public class PaymentServerException extends RuntimeException {

    public PaymentServerException(final String message) {
        super(message);
    }
}
