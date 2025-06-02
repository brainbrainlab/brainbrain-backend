package site.brainbrain.iqtest.infrastructure.payment.nice.util;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import site.brainbrain.iqtest.exception.PaymentException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class NicePaymentValidatorTest {

    @ParameterizedTest
    @CsvSource({"0001", "1000", "500"})
    @DisplayName("결과 코드가 0000이 아니면 예외가 발생한다.")
    void throw_exception_when_result_code_is_not_0000(final String resultCode) {
        assertDoesNotThrow(() -> NicePaymentValidator.validateNiceResultCode("0000"));
        assertThatThrownBy(() -> NicePaymentValidator.validateNiceResultCode(resultCode))
                .isInstanceOf(PaymentException.class)
                .hasMessage("결제 인증에 실패했습니다. result code : " + resultCode);
    }

    @Test
    @DisplayName("위변조 데이터가 다르면 예외가 발생한다.")
    void throw_exception_when_signature_is_different() {
        // given
        final String signature = "signature";
        final String differentSignature = "differentSignature";

        //when & then
        assertDoesNotThrow(() -> NicePaymentValidator.validateSignature(signature, signature));
        assertThatThrownBy(() -> NicePaymentValidator.validateSignature(signature, differentSignature))
                .isInstanceOf(PaymentException.class)
                .hasMessage("위변조 검증 데이터가 일치하지 않습니다.");
    }
}
