package site.brainbrain.iqtest.infrastructure.payment.nice.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SignatureGeneratorTest {

    @Test
    @DisplayName("SHA-256 해시를 생성한다")
    void generate_SHA256_hash() {
        // given
        final String authToken = "testAuthToken";
        final String clientId = "testClientId";
        final int amount = 1000;
        final String secretKey = "testSecretKey";

        // when
        final String signature = SignatureGenerator.generate(authToken, clientId, amount, secretKey);

        // then
        assertThat(signature).isEqualTo("93bb84cc3f4c724ba8189006fa58ba38a075538ae03eef16124721a8d2f21869");
    }
}
