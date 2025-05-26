package site.brainbrain.iqtest.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import site.brainbrain.iqtest.exception.PurchaseOptionException;

public class PurchaseOptionTest {

    @DisplayName("대소문자 구분 없이 PurchaseOption 타입으로 변환한다.")
    @ParameterizedTest
    @CsvSource({
            "basic, BASIC",
            "STANDARD, STANDARD",
            "PreMiUm, PREMIUM"
    })
    void to_purchase_option_type(final String name, final PurchaseOption expected) {
        assertThat(PurchaseOption.from(name)).isEqualTo(expected);
    }

    @DisplayName("일치하는 구매 옵션이 없는 경우 예외가 발생한다.")
    @Test
    void invalid_purchase_option_type() {
        // given
        final String name = "invalid";

        // when & then
        assertThatThrownBy(() -> PurchaseOption.from("invalid"))
                .isInstanceOf(PurchaseOptionException.class)
                .hasMessage("지원하지 않는 구매 옵션입니다: " + name);
    }
}
