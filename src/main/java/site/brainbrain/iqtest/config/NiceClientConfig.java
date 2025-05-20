package site.brainbrain.iqtest.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.Builder;

@Configuration
public class NiceClientConfig {

    @Value("${payment.nice.base_url}")
    private String baseUrl;

    @Bean
    public RestClient niceRestClient(final Builder builder) {
        return builder
                .requestFactory(timeoutFactory())
                .baseUrl(baseUrl)
                .build();
    }

    private SimpleClientHttpRequestFactory timeoutFactory() {
        final SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(5));
        factory.setReadTimeout(Duration.ofSeconds(30));
        return factory;
    }
}
