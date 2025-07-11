package site.brainbrain.iqtest.util;

import java.util.Base64;

public class AuthGenerator {

    private static final String CREDENTIAL_BASE64 = "%s:%s";
    private static final String BASIC_FORMAT = "Basic %s";

    /**
     * @param primaryCredential   토스: secretKey / 나이스: clientKey
     * @param secondaryCredential 토스: "" / 나이스: secretKey
     */
    public static String generate(final String primaryCredential, final String secondaryCredential) {
        final String encodedCredential = encodeBase64(primaryCredential, secondaryCredential);
        return String.format(BASIC_FORMAT, encodedCredential);
    }

    private static String encodeBase64(final String primaryCredential, final String secondaryCredential) {
        final String credential = String.format(CREDENTIAL_BASE64, primaryCredential, secondaryCredential);
        return Base64.getEncoder()
                .encodeToString(credential.getBytes());
    }
}
