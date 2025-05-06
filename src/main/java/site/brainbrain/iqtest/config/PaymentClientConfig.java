package site.brainbrain.iqtest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.Builder;

@Configuration
public class PaymentClientConfig {

    @Value("${payment.toss.base_url}")
    private String baseUrl;

    @Bean
    public RestClient restClient(final Builder builder) {
        return builder
                .baseUrl(baseUrl)
                .build();
    }
}
