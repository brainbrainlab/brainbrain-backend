package site.brainbrain.iqtest.service.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import site.brainbrain.iqtest.exception.PaymentClientException;
import site.brainbrain.iqtest.exception.PaymentServerException;
import site.brainbrain.iqtest.service.payment.dto.ApiConfirmRequest;
import site.brainbrain.iqtest.service.payment.dto.ApiConfirmResponse;
import site.brainbrain.iqtest.service.payment.dto.ApiErrorResponse;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentClient {

    public static final String PAYMENT_CONFIRM = "/v1/payments/confirm";

    @Value("${payment.toss.secret}")
    private String apiSecretKey;

    private final RestClient restClient;
    private final AuthGenerator authGenerator;

    public ApiConfirmResponse confirm(final ApiConfirmRequest apiConfirmRequest) {
        final String encodedKey = authGenerator.encodeBase64(apiSecretKey);
        return restClient.post()
                .uri(PAYMENT_CONFIRM)
                .header(HttpHeaders.AUTHORIZATION, authGenerator.buildBasicAuthHeader(encodedKey))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(apiConfirmRequest)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        (request, response) -> handleError(response)
                )
                .body(ApiConfirmResponse.class);
    }

    private void handleError(final ClientHttpResponse response) {
        log.warn("[토스 API] 결제 승인 실패");
        try {
            final HttpStatusCode statusCode = response.getStatusCode();
            final ApiErrorResponse errorBody = parseErrorResponse(response.getBody());
            if (statusCode.is4xxClientError()) {
                throw new PaymentClientException(errorBody.message());
            }
            if (statusCode.is5xxServerError()) {
                throw new PaymentServerException(errorBody.message());
            }
        } catch (final IOException e) {
            throw new PaymentServerException("토스 API의 결제 승인 에러 응답에서 상태코드/바디를 가져오는데 실패하였습니다.");
        }
    }

    private ApiErrorResponse parseErrorResponse(final InputStream bodyStream) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(bodyStream, ApiErrorResponse.class);
    }
}
