package site.brainbrain.iqtest.service.payment;

import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
public class AuthGenerator {

    private static final String CREDENTIAL_BASE64 = "%s:";
    private static final String BASIC_FORMAT = "BASIC %s";

    public String encodeBase64(final String input) {
        final String credential = String.format(CREDENTIAL_BASE64, input);
        return Base64.getEncoder()
                .encodeToString(credential.getBytes());
    }

    public String buildBasicAuthHeader(final String token) {
        return String.format(BASIC_FORMAT, token);
    }
}
