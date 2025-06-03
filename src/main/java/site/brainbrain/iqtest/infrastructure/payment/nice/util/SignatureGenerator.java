package site.brainbrain.iqtest.infrastructure.payment.nice.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import site.brainbrain.iqtest.exception.PaymentServerException;

public class SignatureGenerator {

    public static String generate(final String authToken,
                                  final String clientId,
                                  final Integer amount,
                                  final String secretKey) {
        final String rawData = authToken + clientId + amount + secretKey;
        return hashSha256(rawData);
    }

    public static String generate(final String tid,
                                  final Integer amount,
                                  final String ediDate,
                                  final String secretKey) {
        final String rawData = tid + amount + ediDate + secretKey;
        return hashSha256(rawData);
    }

    private static String hashSha256(final String rawData) {
        final MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (final NoSuchAlgorithmException e) {
            throw new PaymentServerException("SHA-256 해시 알고리즘을 사용할 수 없습니다.");
        }

        final byte[] hash = digest.digest(rawData.getBytes(StandardCharsets.UTF_8));
        final StringBuilder hexString = new StringBuilder();

        for (final byte b : hash) {
            final String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
