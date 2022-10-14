package com.lev_prav.server.util;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class PasswordEncoder {
    private PasswordEncoder() {
    }

    public static String encode(String password) {
        String hash = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.update(password.getBytes());
            byte[] digested = messageDigest.digest();
            hash = DatatypeConverter.printHexBinary(digested);
            hash = hash.substring(0, 100);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }
}
