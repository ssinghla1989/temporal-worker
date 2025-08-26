package com.example.temporalworker.shared.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;

/**
 * Utility class for computing HMAC signatures.
 */
public final class SignatureUtil {
    private static final String DEFAULT_ALGORITHM = "HmacSHA256";

    private SignatureUtil() {}

    /**
     * Compute an HMAC (SHA-256) signature and return hex-encoded string.
     */
    public static String hmacHex(String secret, String message) {
        byte[] raw = hmac(secret, message, DEFAULT_ALGORITHM);
        return toHex(raw);
    }

    /**
     * Compute an HMAC (SHA-256) signature and return Base64 string.
     */
    public static String hmacBase64(String secret, String message) {
        byte[] raw = hmac(secret, message, DEFAULT_ALGORITHM);
        return Base64.getEncoder().encodeToString(raw);
    }

    /**
     * Compute an HMAC signature with the provided JCE algorithm (e.g., HmacSHA1, HmacSHA256).
     */
    public static byte[] hmac(String secret, String message, String algorithm) {
        try {
            Mac mac = Mac.getInstance(algorithm);
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), algorithm);
            mac.init(keySpec);
            return mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Failed to compute HMAC: " + e.getMessage(), e);
        }
    }

    /**
     * Constant-time comparison of two byte arrays.
     */
    public static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a == null || b == null) {
            return false;
        }
        if (a.length != b.length) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }
        return result == 0;
    }

    /**
     * Hex encoding for byte arrays.
     */
    public static String toHex(byte[] bytes) {
        char[] HEX = "0123456789abcdef".toCharArray();
        char[] out = new char[bytes.length * 2];
        for (int i = 0, j = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            out[j++] = HEX[v >>> 4];
            out[j++] = HEX[v & 0x0F];
        }
        return new String(out);
    }
}


