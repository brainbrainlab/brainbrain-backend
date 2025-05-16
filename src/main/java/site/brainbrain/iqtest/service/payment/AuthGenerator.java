package site.brainbrain.iqtest.service.payment;

import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
public class AuthGenerator {

    private static final String CREDENTIAL_BASE64 = "%s:%s";
    private static final String BASIC_FORMAT = "Basic %s";

    /**
     * @param primaryCredential  토스: secretKey / 나이스: clientKey
     * @param secondaryCredential 토스: "" / 나이스: secretKey
     */
    public String encodeBase64(final String primaryCredential, final String secondaryCredential) {
        final String credential = String.format(CREDENTIAL_BASE64, primaryCredential, secondaryCredential);
        return Base64.getEncoder()
                .encodeToString(credential.getBytes());
    }

    public String buildBasicAuthHeader(final String token) {
        return String.format(BASIC_FORMAT, token);
    }
}
