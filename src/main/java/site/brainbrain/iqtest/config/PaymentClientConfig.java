package site.brainbrain.iqtest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class PaymentClientConfig {

    @Value("${payment.toss.base_url}")
    private String baseUrl;

    @Bean
    public RestClient restClient(final RestClient.Builder builder) {
        return builder
                .baseUrl(baseUrl)
                .build();
    }
}
