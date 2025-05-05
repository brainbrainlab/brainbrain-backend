package site.brainbrain.iqtest.service.payment;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import site.brainbrain.iqtest.config.PaymentClientConfig;
import site.brainbrain.iqtest.exception.PaymentClientException;
import site.brainbrain.iqtest.exception.PaymentServerException;
import site.brainbrain.iqtest.service.payment.dto.ApiConfirmRequest;
import site.brainbrain.iqtest.service.payment.dto.ApiErrorResponse;

@RestClientTest({PaymentClient.class, PaymentClientConfig.class, AuthGenerator.class})
class PaymentClientTest {

    private static final ApiConfirmRequest REQUEST = new ApiConfirmRequest("test_pk", 19000, "test_oi");
    private static final String REQUEST_JSON;
    static {
        try {
            REQUEST_JSON = new ObjectMapper().writeValueAsString(REQUEST);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Value("${payment.toss.base_url}")
    private String baseUrl;

    @Value("${payment.toss.secret}")
    private String apiSecretKey;

    @Autowired
    private PaymentClient paymentClient;

    @Autowired
    private AuthGenerator authGenerator;

    @Autowired
    private MockRestServiceServer mockServer;

    @Test
    @DisplayName("결제 승인에 성공한다.")
    void paymentConfirm_success() {
        // given
        final String endpoint = makeEndpoint(PaymentApiUri.CONFIRM);
        final String encodedKey = authGenerator.encodeBase64(apiSecretKey);
        mockServer.expect(requestTo(endpoint))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HttpHeaders.AUTHORIZATION, authGenerator.buildBasicAuthHeader(encodedKey)))
                .andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(REQUEST_JSON))
                .andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));

        // when & then
        assertDoesNotThrow(() -> paymentClient.confirm(REQUEST));
    }

    @Test
    @DisplayName("결제 승인에 실패하여 4XX 에러를 응답하면 Client 커스텀 예외를 던진다.")
    void paymentConfirm_fail_4xx() throws JsonProcessingException {
        // given
        final String endpoint = makeEndpoint(PaymentApiUri.CONFIRM);
        final String encodedKey = authGenerator.encodeBase64(apiSecretKey);
        final ApiErrorResponse response = new ApiErrorResponse("NOT_FOUND_PAYMENT_SESSION", "결제 시간이 만료되어 결제 진행 데이터가 존재하지 않습니다.");
        mockServer.expect(requestTo(endpoint))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HttpHeaders.AUTHORIZATION, authGenerator.buildBasicAuthHeader(encodedKey)))
                .andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(REQUEST_JSON))
                .andRespond(withBadRequest().body(new ObjectMapper().writeValueAsString(response)));

        // when & then
        assertThatThrownBy(() -> paymentClient.confirm(REQUEST)).isExactlyInstanceOf(PaymentClientException.class);
    }

    @Test
    @DisplayName("결제 승인에 실패하여 5XX 에러를 응답하면 Server 커스텀 예외를 던진다.")
    void paymentConfirm_fail_5xx() throws JsonProcessingException {
        // given
        final String endpoint = makeEndpoint(PaymentApiUri.CONFIRM);
        final String encodedKey = authGenerator.encodeBase64(apiSecretKey);
        final ApiErrorResponse response = new ApiErrorResponse("FAILED_PAYMENT_INTERNAL_SYSTEM_PROCESSING", "결제가 완료되지 않았어요. 다시 시도해주세요.");
        mockServer.expect(requestTo(endpoint))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HttpHeaders.AUTHORIZATION, authGenerator.buildBasicAuthHeader(encodedKey)))
                .andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(REQUEST_JSON))
                .andRespond(withServerError().body(new ObjectMapper().writeValueAsString(response)));

        // when & then
        assertThatThrownBy(() -> paymentClient.confirm(REQUEST)).isExactlyInstanceOf(PaymentServerException.class);
    }

    private String makeEndpoint(final PaymentApiUri uri) {
        return baseUrl + uri.getUri();
    }
}
