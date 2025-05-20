package site.brainbrain.iqtest.infrastructure.payment.nice;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import site.brainbrain.iqtest.exception.PaymentClientException;
import site.brainbrain.iqtest.exception.PaymentServerException;
import site.brainbrain.iqtest.infrastructure.payment.nice.dto.NiceApiCancelResponse;
import site.brainbrain.iqtest.infrastructure.payment.nice.dto.NiceApiConfirmResponse;
import site.brainbrain.iqtest.infrastructure.payment.nice.dto.NiceApproveRequest;
import site.brainbrain.iqtest.infrastructure.payment.nice.dto.NiceCancelRequest;
import site.brainbrain.iqtest.infrastructure.payment.nice.dto.NicePaymentCallbackRequest;
import site.brainbrain.iqtest.util.AuthGenerator;
import site.brainbrain.iqtest.service.payment.dto.ApiErrorResponse;

@Slf4j
@Component
public class NicePaymentClient {

    private static final String PAYMENT_CONFIRM = "/v1/payments/%s";
    private static final String PAYMENT_CANCEL = "v1/payments/%s/cancel";

    private final String apiSecretKey;
    private final RestClient restClient;

    public NicePaymentClient(@Value("${payment.nice.secret}") final String apiSecretKey,
                             @Qualifier("niceRestClient") final RestClient restClient) {
        this.apiSecretKey = apiSecretKey;
        this.restClient = restClient;
    }

    public NiceApiConfirmResponse confirm(final NicePaymentCallbackRequest callbackRequest) {
        try {
            return approve(callbackRequest);
        } catch (final RestClientException e) {
            log.error("나이스 결제 승인 요청 중 예외 발생: {}", e.getMessage());
            cancel(callbackRequest);
            throw new PaymentServerException("나이스 결제 승인 요청 중 에러가 발생했습니다.");
        }
    }

    private NiceApiConfirmResponse approve(final NicePaymentCallbackRequest callbackRequest) {
        return restClient.post()
                .uri(String.format(PAYMENT_CONFIRM, callbackRequest.tid()))
                .header(HttpHeaders.AUTHORIZATION, AuthGenerator.generate(callbackRequest.clientId(), apiSecretKey))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(new NiceApproveRequest(callbackRequest.amount()))
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        (request, response) -> handleError(response))
                .body(NiceApiConfirmResponse.class);
    }

    private void cancel(final NicePaymentCallbackRequest callbackRequest) {
        try {
            final NiceApiCancelResponse cancelResponse = restClient.post()
                    .uri(String.format(PAYMENT_CANCEL, callbackRequest.tid()))
                    .header(HttpHeaders.AUTHORIZATION, AuthGenerator.generate(callbackRequest.clientId(), apiSecretKey))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(new NiceCancelRequest("결제 승인 중 타임아웃으로 인한 취소", callbackRequest.orderId()))
                    .retrieve()
                    .onStatus(HttpStatusCode::isError,
                            (request, response) -> handleError(response))
                    .body(NiceApiCancelResponse.class);
            log.info("나이스 결제 취소 성공: {}", cancelResponse);
        } catch (final Exception e) {
            log.error("나이스 결제 취소 중 예외 발생: {}", e.getMessage());
            throw new PaymentServerException("결제 취소 중 에러가 발생했습니다.");
        }
    }

    private void handleError(final ClientHttpResponse response) {
        log.warn("[나이스페이먼츠 API] 결제 승인 실패");
        try {
            final HttpStatusCode statusCode = response.getStatusCode();
            System.out.println(statusCode);
            final ApiErrorResponse errorBody = parseErrorResponse(response.getBody());
            if (statusCode.is4xxClientError()) {
                throw new PaymentClientException(errorBody.message());
            }
            if (statusCode.is5xxServerError()) {
                throw new PaymentServerException(errorBody.message());
            }
        } catch (final IOException e) {
            throw new PaymentServerException("나이스 결제 승인 에러 응답에서 상태코드/바디를 가져오는데 실패하였습니다.");
        }
    }

    private ApiErrorResponse parseErrorResponse(final InputStream bodyStream) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(bodyStream, ApiErrorResponse.class);
    }
}
